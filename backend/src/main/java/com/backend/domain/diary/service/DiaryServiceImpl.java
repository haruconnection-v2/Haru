package com.backend.domain.diary.service;

import static com.backend.domain.diary.mapper.DiaryMapper.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domain.calendar.entity.Calendar;
import com.backend.domain.calendar.service.CalendarService;
import com.backend.domain.chat.entity.HaruRoom;
import com.backend.domain.chat.repository.HaruRoomRepository;
import com.backend.domain.diary.dto.DiaryCreateRequest;
import com.backend.domain.diary.dto.DiaryRequest;
import com.backend.domain.diary.dto.DiaryResponse;
import com.backend.domain.diary.dto.DiarySnsDto;
import com.backend.domain.diary.entity.Diary;
import com.backend.domain.diary.repository.DiaryRepository;
import com.backend.global.common.exception.BadRequestException;
import com.backend.global.common.exception.ConflictException;
import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class DiaryServiceImpl implements DiaryService {

	private final DiaryRepository diaryRepository;
	private final CalendarService calendarService;
	private final HaruRoomRepository haruRoomRepository;

	//mapper에 member.nickname 추가예정
	//session 로직 추가 예정
	@Override
	public DiaryResponse getDiaryDetailData(final Long diaryId, final HttpServletRequest request) {
		final Diary foundDiary = diaryRepository.findById(diaryId).orElseThrow(() -> new NotFoundException(ErrorCode.DIARY_NOT_FOUND));
		final String nickname = getNicknameFromSession(request);
		return toDetailDiaryResponse(foundDiary, nickname);
	}

	@Override
	public DiaryResponse getDiarySnsLink(final String day, final HttpServletRequest request) {
		final Long calendarId = (Long)request.getSession().getAttribute("calendarId");
		if (calendarId == null) {
			throw new NotFoundException(ErrorCode.CALENDAR_NOT_FOUND);
		}
		final DiarySnsDto diarySnsDto = diaryRepository.findDiarySnsByCalendarIdAndDay(calendarId, day);
		if (diarySnsDto.isExpiry()) {
			throw new BadRequestException(ErrorCode.DIARY_GONE);
		}
		final String nickname = request.getSession().getAttribute("nickname").toString();
		//diaryResponse = setMemberNickname;
		return toSnsDiaryResponse(diarySnsDto, nickname);
	}

	private String getNicknameFromSession(final HttpServletRequest request) {
		final Object nickname = request.getSession().getAttribute("nickname");
		if (nickname == null) {
			return "undefined";
		}
		return nickname.toString();
	}

	@Override
	@Transactional
	public DiaryResponse createDiary(final DiaryCreateRequest diaryRequest, final HttpServletRequest request) {
		final HttpSession session = request.getSession();
		final Calendar calendar = calendarService.getOrCreateCalendar(session);
		validateDiaryNotExist(calendar.getId(), diaryRequest.getDay());

		// diary, haruRoom entity 연관관계로 인해 diary 생성 후 link를 생성해야 함.
		final Diary diary = diaryRepository.save(createDiaryEntity(diaryRequest, session, calendar));
		final HaruRoom haruRoom = generateHaruRoom(diary);

		final String snsLink = generateSnsLink(request, haruRoom.getId());
		diary.updateDiaryLink(snsLink);
		diaryRepository.save(diary);

		final String nickname = session.getAttribute("nickname").toString();
		return toCreateDiaryResponse(diary, nickname);
	}

	private void validateDiaryNotExist(final Long calendarId, final String day) {
		if (diaryRepository.existsByCalendarIdAndDay(calendarId, day)) {
			throw new ConflictException(ErrorCode.DIARY_CONFLICT);
		}
	}

	private String generateSnsLink(final HttpServletRequest request, final Long roomId) {
		final String host = request.getServerName();
		final int port = request.getServerPort();
		return String.format("http://%s:%d/rooms/%d", host, port, roomId);
	}

	private Diary createDiaryEntity(final DiaryCreateRequest diaryRequest, final HttpSession session, Calendar calendar) {
		return Diary.builder()
			.calendar(calendar)
			.diaryBgId(diaryRequest.getDiaryBgId())
			.day(diaryRequest.getDay())
			.monthYear(session.getAttribute("monthYear").toString())
			.isExpiry(false)
			.build();
	}

	private HaruRoom generateHaruRoom(Diary diary) {
		HaruRoom haruRoom = HaruRoom.builder()
			.diary(diary)
			.build();
		return haruRoomRepository.save(haruRoom);
	}

	@Override
	@Transactional
	public void saveFinallyDiary(Long diaryId, DiaryRequest diaryRequest) {
		Diary diary = diaryRepository.findById(diaryId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.DIARY_NOT_FOUND));
		diary.expireDiary();
		diaryRepository.save(updateDiaryTextAndImageBoxes(diary, diaryRequest));
	}
}