package com.backend.domain.image;

import static com.backend.global.common.response.ResultCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.global.common.exception.NotFoundException;
import com.backend.global.common.response.ErrorCode;
import com.backend.global.common.response.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StaticImageController {

	private final StaticImageRepository staticImageRepository;
	@GetMapping("/static/stickers")
	public ResponseEntity<ResultResponse<String>> getStaticImage(
		@RequestParam("page") Long stickerPageNum) {
		String stBgImgUrl = String.valueOf(staticImageRepository.findById(stickerPageNum)
																.orElseThrow(() -> new NotFoundException(ErrorCode.STICKER_NOT_FOUND)));
		ResultResponse<String> resultResponse = ResultResponse.of(FIND_ST_STICKER_SUCCESS, stBgImgUrl);
		return ResponseEntity.status(FIND_ST_STICKER_SUCCESS.getHttpStatus()).body(resultResponse);
	}
}
