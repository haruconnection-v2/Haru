import React, { useState, useEffect } from 'react';
import { baseInstance } from '../api/config';

function OpenDiary({ diaryId }) {
  const [diaryData, setDiaryData] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await baseInstance.get(
            `/diaries/${diaryId}`,
            {
              diaryId: diaryId,
            }
          );
          if (response.data.code === 'D001' && response.status === 200) {
            console.log('일기장 조회 성공');           
            setDiaryData(response.data.data);
        } else {
            console.log('일기장 조회 실패');
          }
      } catch (error) {
        console.error('API 호출 중 오류 발생 : ', error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [diaryId]);//이건 굳이 안넣어도 되나


  //이거 어떻게 할지 방식 다시 생각해보자
  const renderTextboxes = () => {
    if (!diaryData || !diaryData.diaryTextBoxs) {
      return null;
    }

    return diaryData.diaryTextBoxs.map((textbox) => (
      <div key={textbox.textboxId}>
        <p>{textbox.content}</p>
      </div>
    ));
  };

  const renderStickers = () => {
    if (!diaryData || !diaryData.diaryStickers) {
      return null;
    }

    return diaryData.diaryStickers.map((sticker) => (
      <div key={sticker.stickerId}>
        <img src={sticker.stickerImageUrl} alt="스티커 이미지" />
      </div>
    ));
  };

  return (
    <div>
      <button onClick={() => fetchData()} disabled={loading}>
        {loading ? '로딩 중...' : '일기 조회'}
      </button>
      {diaryData && (
        <div>
          <h2>{diaryData.diaryDate}</h2>
          <img src={diaryData.diaryBgUrl} alt="일기 배경 이미지" />
          {renderTextboxes()}
          {renderStickers()}
        </div>
      )}
    </div>
  );
}

export default OpenDiary;
