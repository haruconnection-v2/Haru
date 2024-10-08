import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { useSelectDateInfoStore } from '../../src/stores/useSelectDateInfoStore';
import { useParams } from 'react-router-dom';
import LargeSketchbook from '../components/LargeSketchbook';
import NavigateBar from '../components/NavigateBar';
import BasicSticker from '../components/BasicSticker';
import RightSticker from '../components/DiaryPage/RightSticker';
import DHomeButton from '../components/DiaryPage/DHomeButton';
import SaveButton from '../components/DiaryPage/SaveButton';
import TextButton from '../components/DiaryPage/TextButton';

import InnerImg from '../components/DiaryPage/InnerImg';
import useStickerStore from '../stores/stickerStore';
import useTextStore from '../stores/textStore';
import useDalleStore from '../stores/dalleStore';
import useUserInfoStore from '../stores/userInfoStore';

const setMetaTags = ({
  title = '하루연결', // 기본 타이틀
  description = '“1월 30일”의 일상을 공유해요!', // 기본 설명
  imageUrl = 'https://i.postimg.cc/DZYT5Y2J/Share-Icon.png', // 기본 사이트 이미지 경로
}) => {
  const titleTag = document.querySelector('meta[property="og:title"]'); // document.querySelector를 사용하여 index.html의 해당 메타 태그를 선택

  // 해당하는 메타 태그가 없다면 document.querySelector는 null을 반환하게 되고, 그러고 .setAttribute 메서드를 호출하려 하면 오류가 발생
  if (titleTag) {
    // 따라서 if문으로 메타 태그가 존재하는지 확인한 후에 .setAttribute를 호출해야 함
    titleTag.setAttribute('content', `${title}`);
  }

  const descriptionTag = document.querySelector(
    'meta[property="og:description"]',
  );
  if (descriptionTag) {
    descriptionTag.setAttribute('content', description);
  }

  const imageTag = document.querySelector('meta[property="og:image"]');
  if (imageTag) {
    imageTag.setAttribute('content', imageUrl);
  }

  const urlTag = document.querySelector('meta[property="og:url"]');
  if (urlTag) {
    urlTag.setAttribute('content', window.location.href);
  }
};
//-----------------------------------------------------------------------

function DiaryPage() {
  const [selectedTextBox, setSelectedTextBox] = useState(false);
  const [selectedSticker, setSelectedSticker] = useState(null);
  const [selectedDalle, setSelectedDalle] = useState(null);
  const selectedDateInfo = useSelectDateInfoStore((state) => state);
  const { diaryId } = useParams();
  const websocket = useRef(null);
  const { userInfoList, addUserInfo, getUserInfo, removeUserInfo } =
    useUserInfoStore();
  const userId = userInfoList.map((user) => user.id);
  const [hostCheck, setHostCheck] = useState(true);
  const [hostId, setHostId] = useState('');
  const [savedData, setSavedData] = useState({
    textboxs: [],
    stickers: [],
  });

  useEffect(() => {
    if (userId == hostId) {
      setHostCheck(true);
    } else {
      setHostCheck(false);
    }
  }, [userId]);

  const handleTextButtonClick = () => {
    setSelectedTextBox(true);
  };

  const handleStickerSelect = (image) => {
    setSelectedSticker(image); // 선택한 이미지 URL을 상태로 저장
  };

  const handleDalleSelect = (image) => {
    setSelectedDalle(image);
  };

  useEffect(() => {
    if (!diaryId) {
      console.log('diaryId가 설정되지 않았습니다.');
      return;
    }

    const newSocket = new WebSocket(
      //`wss://${window.location.host}/ws/harurooms/${diaryId}/`, // 배포용
      `ws://127.0.0.1:8000/ws/harurooms/${diaryId}/`, // 개발용
    );
    websocket.current = newSocket;

    // 웹소켓 연결이 성공했을 때
    newSocket.onopen = () => {
      console.log('WebSocket 연결됨');
    };

    // 웹소켓 연결이 끊어졌을 때
    newSocket.onclose = (event) => {
      if (event.wasClean) {
        console.log(`WebSocket 연결이 정상적으로 종료됨: ${event.code}`);
      }
    };

    // WebSocket 이벤트 리스너 설정
    newSocket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      if (data.type === 'create_sticker') {
        console.log('스티커 생성');
        useStickerStore.getState().addSticker({
          id: data.sticker_id,
          image: data.image,
          ...data.position,
        });
      } else if (data.type === 'image_drag') {
        console.log('드래그 발생');
        useStickerStore
          .getState()
          .updateSticker({ id: data.sticker_id, ...data.position });
      } else if (data.type === 'image_resize') {
        console.log('리사이즈 발생');
        useStickerStore.getState().updateSticker({
          id: data.sticker_id,
          ...data.position,
        });
      } else if (data.type === 'image_rotate') {
        console.log('로테이트 발생');
        useStickerStore
          .getState()
          .updateSticker({ id: data.sticker_id, ...data.position });
      } else if (data.type === 'delete_object') {
        console.log('삭제');
        useStickerStore.getState().deleteSticker(data.object_id);
        useTextStore.getState().deleteText(data.object_id);
        useDalleStore.getState().deleteDalle(data.object_id);
      } else if (data.type === 'save_sticker') {
        console.log('스티커 저장');
        useStickerStore.getState().updateSticker({
          id: data.sticker_id,
          image: data.image,
          showOnly: true,
          ...data.position,
        });
        setSavedData((prevData) => ({
          ...prevData,
          stickers: [
            ...prevData.stickers,
            {
              sticker_id: data.sticker_id,
              sticker_image_url: data.image,
              top: data.position.top2,
              left: data.position.left2,
              height: data.position.height2,
              width: data.position.width2,
              rotate: data.position.rotate2 || 0, // rotate가 없는 경우 기본값 0
            },
          ],
        }));
      }

      // Dalle
      if (data.type === 'create_dalle') {
        console.log('Dalle 생성');
        useDalleStore.getState().addDalle({
          id: data.dalle_id,
          image: data.image,
          ...data.position,
        });
      } else if (data.type === 'dalle_drag') {
        console.log('dalle 드래그 발생');
        useDalleStore
          .getState()
          .updateDalle({ id: data.dalle_id, ...data.position });
      } else if (data.type === 'dalle_resize') {
        console.log('dalle 리사이즈 발생');
        useDalleStore.getState().updateDalle({
          id: data.dalle_id,
          ...data.position,
        });
      } else if (data.type === 'dalle_rotate') {
        console.log('dalle 로테이트 발생');
        useDalleStore
          .getState()
          .updateDalle({ id: data.dalle_id, ...data.position });
      } else if (data.type === 'save_dalle') {
        console.log('Dalle 저장');
        useDalleStore.getState().updateDalle({
          id: data.dalle_id,
          image: data.image,
          showOnly: true,
          ...data.position,
        });
        setSavedData((prevData) => ({
          ...prevData,
          stickers: [
            ...prevData.stickers,
            {
              sticker_id: data.dalle_id,
              sticker_image_url: data.image,
              top: data.position.top2,
              left: data.position.left2,
              height: data.position.height2,
              width: data.position.width2,
              rotate: data.position.rotate2 || 0, // rotate가 없는 경우 기본값 0
            },
          ],
        }));
      }

      // 텍스트 박스
      if (data.type === 'create_textbox') {
        // console.log('텍스트 박스 생성');
        useTextStore.getState().addText({
          id: data.text_id,
          ...data.position,
        });
      } else if (data.type === 'text_drag') {
        // console.log('텍스트 드래그 발생');
        useTextStore
          .getState()
          .updateText({ id: data.text_id, ...data.position });
      } else if (data.type === 'text_resize') {
        // console.log('텍스트 리사이즈 발생');
        useTextStore
          .getState()
          .updateText({ id: data.text_id, ...data.position });
      } else if (data.type === 'text_input') {
        console.log('텍스트 입력 발생');
        console.log('입력값:', data.content);
        useTextStore
          .getState()
          .updateText({ id: data.text_id, content: data.content });
      } else if (data.type === 'nickname_input') {
        console.log('닉네임 입력 발생');
        console.log('입력값:', data.nickname);
        useTextStore
          .getState()
          .updateText({ id: data.text_id, nickname: data.nickname });
      } else if (data.type === 'save_text') {
        console.log('save_text');
        useTextStore.getState().updateText({
          id: data.text_id,
          content: data.content,
          nickname: data.nickname,
          showOnly: true,
          ...data.position,
        });
        setSavedData((prevData) => ({
          ...prevData,
          textboxs: [
            ...prevData.textboxs,
            {
              textbox_id: data.text_id,
              writer: data.nickname,
              xcoor: data.position.x,
              ycoor: data.position.y,
              height: data.position.height,
              width: data.position.width,
            },
          ],
        }));
        console.log('텍스트 저장', data.content, data.nickname);
      }
    };
    setMetaTags({
      title: '하루연결',
      description: '“1월 30일”의 일상을 공유해요!',
      imageUrl: 'https://i.postimg.cc/DZYT5Y2J/Share-Icon.png',
    });

    return () => {
      newSocket.close();
      console.log('WebSocket 연결 종료');
    };
  }, []);

  return (
    <BackLayout>
      <PageFrame>
        <WrapperNavigateBar>
          <NavigateBar locate={'diary'} />
        </WrapperNavigateBar>
        <WrapperLargeSketchbook>
          <LargeSketchbook />
        </WrapperLargeSketchbook>
        <WrapperInnerImg>
          <InnerImg
            websocket={websocket}
            diaryMonth={selectedDateInfo.selectedMonth}
            diaryDay={selectedDateInfo.selectedDay}
            diaryId={diaryId}
            setHostId={setHostId}
          />
        </WrapperInnerImg>
        <WrapperRightSticker>
          <RightSticker
            diaryMonth={selectedDateInfo.selectedMonth}
            diaryDay={selectedDateInfo.selectedDay}
            onDalleSelect={handleDalleSelect}
            websocket={websocket}
          />
        </WrapperRightSticker>
        <WrapperDHomeButton>{hostCheck && <DHomeButton />}</WrapperDHomeButton>
        <WrapperSaveButton>
          {hostCheck && <SaveButton savedData={savedData} />}
        </WrapperSaveButton>
        <WrapperBasicSticker>
          <BasicSticker
            onStickerSelect={handleStickerSelect}
            websocket={websocket}
          />
        </WrapperBasicSticker>
        <TextButton onClick={handleTextButtonClick} websocket={websocket} />
      </PageFrame>
    </BackLayout>
  );
}

const BackLayout = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  flex-shrink: 0;
  background: linear-gradient(to bottom, #c1e3ff 60%, #ffffff);
  justify-content: center;
  align-items: center;
  display: flex;
  overflow-y: hidden;
`;
const PageFrame = styled.div`
  position: absolute;
  width: 108rem;
  height: 70rem;
  display: flex;
  top: 0;
  justify-content: center;
`;

const WrapperNavigateBar = styled.div`
  position: absolute;
  top: -0.6rem;
`;
const WrapperLargeSketchbook = styled.div`
  position: absolute;
  top: 6.9375rem;
`;
const WrapperBasicSticker = styled.div`
  position: absolute;
  top: 13.5rem;
  left: 3.55rem;
`;
const WrapperRightSticker = styled.div`
  position: absolute;
  top: 16.87rem;
  margin-left: 86.2rem;
  margin-top: -3.4rem;
`;

const WrapperDHomeButton = styled.div`
  position: absolute;
  right: 2rem;
  top: 56.6rem;
  display: flex;
  z-index: 10;
`;

const WrapperSaveButton = styled.div`
  position: absolute;
  right: 6.4rem;
  top: 56.8rem;
  display: flex;
  z-index: 10;
`;

const WrapperInnerImg = styled.div`
  position: absolute;
  margin-top: -4rem;
  left: 22.7rem;
`;

export default DiaryPage;