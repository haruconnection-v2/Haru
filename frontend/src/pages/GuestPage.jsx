import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

function GuestPage() {
  const navigate = useNavigate();
  const { diaryId } = useParams();

  useEffect(() => {
    localStorage.setItem('loggedInUserNickname', 'undefined');
    const location = window.location;
    //const link = `${location.protocol}//${location.host}/diary/${diaryId}`;
    navigate(`/diary/${diaryId}`);
  }, [diaryId, navigate]);

  return null; // UI를 렌더링하지 않음
}

export default GuestPage;