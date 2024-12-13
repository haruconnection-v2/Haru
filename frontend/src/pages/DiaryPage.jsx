import React, {useEffect, useRef, useState} from 'react';
import styled from 'styled-components';
import {useSelectDateInfoStore} from '../../src/stores/useSelectDateInfoStore';
import {useParams} from 'react-router-dom';
import LargeSketchbook from '../components/LargeSketchbook';
import NavigateBar from '../components/NavigateBar';
import BasicSticker from '../components/BasicSticker';
import DHomeButton from '../components/DiaryPage/DHomeButton';
import SaveButton from '../components/DiaryPage/SaveButton';
import TextButton from '../components/DiaryPage/TextButton';

import InnerImg from '../components/DiaryPage/InnerImg';
import useTextStore from '../stores/textStore';
import useUserInfoStore from '../stores/userInfoStore';
import useWebSocket from "../util/WebSocketConfig.js";
import {handleEvent} from "../handler/EventHandler.js";
import {textBoxActions} from "../stores/storeHelper.js"; // STOMP 사용

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
  const { userInfoList } = useUserInfoStore();
  const userId = userInfoList.map((user) => user.id);
  const [hostCheck, setHostCheck] = useState(true);
  const [hostId, setHostId] = useState('');
  const [savedData, setSavedData] = useState({
    textboxs: [],
    stickers: [],
  });

  useEffect(() => {
    setHostCheck(true);
/*    if (userId == hostId) {
      setHostCheck(true);

    } else {
      setHostCheck(false);
    }*/
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

  const stompClient =  useWebSocket(diaryId, handleEvent, nickname);

  setMetaTags({
    title: '하루연결',
    description: '“1월 30일”의 일상을 공유해요!',
    imageUrl: 'https://i.postimg.cc/DZYT5Y2J/Share-Icon.png',
  });

  return (
      <BackLayout>
        <PageFrame>
          <WrapperNavigateBar>
            <NavigateBar locate={'diary'}/>
          </WrapperNavigateBar>
          <WrapperLargeSketchbook>
            <LargeSketchbook/>
          </WrapperLargeSketchbook>
          <WrapperInnerImg>
            <InnerImg
                websocket={stompClient}
                diaryMonth={selectedDateInfo.selectedMonth}
                diaryDay={selectedDateInfo.selectedDay}
                diaryId={diaryId}
                setHostId={setHostId}
            />
          </WrapperInnerImg>
          <WrapperRightSticker>
            {/* <RightSticker
            diaryMonth={selectedDateInfo.selectedMonth}
            diaryDay={selectedDateInfo.selectedDay}
            onDalleSelect={handleDalleSelect}
            websocket={stompClient}
          /> */}
          </WrapperRightSticker>
          <WrapperDHomeButton>{hostCheck && <DHomeButton />}</WrapperDHomeButton>
          <WrapperSaveButton>
            {hostCheck && <SaveButton savedData={savedData}/>}
          </WrapperSaveButton>
          <WrapperBasicSticker>
            <BasicSticker
                onStickerSelect={handleStickerSelect}
                websocket={stompClient}
            />
          </WrapperBasicSticker>
          <TextButton
              onClick={handleTextButtonClick}
              websocket={stompClient}
              diaryId={diaryId}
          />
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
