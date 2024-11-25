import React, { useState, useEffect } from 'react';
import ResizableRect from 'react-resizable-rotatable-draggable';
import styled from 'styled-components';
import Swal from 'sweetalert2/dist/sweetalert2.js';
import 'sweetalert2/src/sweetalert2.scss';
import useDalleStore from '../../stores/dalleStore';
import xclose from '../../assets/img/xclose.png';

function DalleSticker({ dalleId, image, bounds, websocket }) {
  const dalles = useDalleStore((state) => state.dalles);
  const dalle = dalles.find((d) => d.id === dalleId);

  //----------------------------------------------------------------
  const ImgSaveClick = ({ websocket, dalleId, image, postion }) => {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger',
      },
      buttonsStyling: true,
    });

    swalWithBootstrapButtons
      .fire({
        title: '저장하실 건가요?',
        text: '한번 저장하면 내용을 수정할 수 없어요!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: '네, 저장할게요!',
        cancelButtonText: '아니요, 나중에 할게요!',
        reverseButtons: true,
      })
      .then((result) => {
        if (result.isConfirmed) {
          websocket.current.send(
            JSON.stringify({
              type: 'saveDalle',
              id: dalleId,
              image: image,
              position: {
                topPos: dalle.top2,
                leftPos: dalle.left2,
                width: dalle.width2,
                height: dalle.height2,
                rotate: dalle.rotate2,
              },
            }),
          );
          swalWithBootstrapButtons.fire({
            title: '저장되었어요!',
            icon: 'success',
          });
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          swalWithBootstrapButtons.fire({
            title: '저장되지 않았어요',
            icon: 'error',
          });
        }
      });
  };
  //--------------------------------------------------------------
  if (dalle.showOnly) {
    return (
      <div
        style={{
          position: 'absolute',
          top: dalle.top2 + 'px',
          left: dalle.left2 + 'px',
          rotate: dalle.rotate2 + 'deg',
        }}>
        <img
          src={image}
          style={{
            width: dalle.width2 + 'px',
            height: dalle.height2 + 'px',
          }}
        />
      </div>
    );
  }

  // WebSocket 메시지 전송 함수
  const sendWebSocketMessage = (type, updatedPosition, objectType) => {
    websocket.current.send(
      JSON.stringify({
        type,
        id: dalleId,
        objectType: objectType,
        position: updatedPosition,
      }),
    );
  };

  const handleDrag = (deltaX, deltaY) => {
    const boundsRect = bounds.current.getBoundingClientRect();

    let newTop = dalle.top2 + deltaY;
    let newLeft = dalle.left2 + deltaX;

    const roundedTop = Math.round(newTop);
    const roundedLeft = Math.round(newLeft);

    sendWebSocketMessage('dalleDrag', {
      topPos: roundedTop,
      leftPos: roundedLeft,
    });
  };

  const handleResize = (style, isShiftKey, type) => {
    const position = {
      width: Math.round(style.width),
      height: Math.round(style.height),
      topPos: Math.round(style.top),
      leftPos: Math.round(style.left),
    };

    sendWebSocketMessage('dalleResize', position);
  };

  const handleRotate = (rotateAngle) => {
    console.log('회전');
    const roundedRotate = Math.round(rotateAngle);
    sendWebSocketMessage('dalleRotate', { rotate: roundedRotate });
  };

  const handleDragStop = () => {
    const objectType = 'dalle';
    const dalleData = {
      image: image,
      width: dalle.width2,
      height: dalle.height2,
      topPos: dalle.top2,
      leftPos: dalle.left2,
      rotate: dalle.rotate2,
    };
    useDalleStore.getState().updateDalle(dalleData);
    sendWebSocketMessage('dragStop', objectType, dalleData);
  };

  const handleResizeStop = () => {
    const objectType = 'dalle';
    const dalleData = {
      image: image,
      width: dalle.width2,
      height: dalle.height2,
      topPos: dalle.top2,
      leftPos: dalle.left2,
      rotate: dalle.rotate2,
    };
    useDalleStore.getState().updateDalle(dalleData);
    sendWebSocketMessage('resizeStop', objectType, dalleData);
  };

  const handleRotateStop = () => {
    const objectType = 'dalle';
    const dalleData = {
      image: image,
      width: dalle.width2,
      height: dalle.height2,
      topPos: dalle.top2,
      leftPos: dalle.left2,
      rotate: dalle.rotate2,
    };
    useDalleStore.getState().updateDalle(dalleData);
    sendWebSocketMessage('rotateStop', objectType, dalleData);
  };

  const onDelete = () => {
    // 서버로 삭제 요청 보내기
    websocket.current.send(
      JSON.stringify({
        type: 'deleteObject',
        objectType: 'dalle',
        objectId: dalleId,
      }),
    );
  };

  return (
    <>
      <div
        style={{
          width: dalle.width2,
          height: dalle.height2,
          position: 'absolute',
          zIndex: 1,
        }}>
        <CloseButton
          onClick={onDelete}
          style={{
            left: dalle.left2 + dalle.width2 - 20,
            top: dalle.top2 - 10,
            zIndex: 200,
          }}>
          <img
            style={{
              width: '1rem',
              height: '1rem',
              top: '0.4rem',
              left: '0.5rem',
              position: 'absolute',
            }}
            src={xclose}
            alt="close"
          />
        </CloseButton>
        <ImgSaveBtn
          onClick={() =>
            ImgSaveClick({
              websocket: websocket,
              dalleId: dalle.id,
              image: image,
              position: {
                width: dalle.width2,
                height: dalle.height2,
                topPos: dalle.top2 + 1,
                leftPos: dalle.left2 + 1,
                rotate2: `${dalle.rotate2}deg`,
              },
            })
          }
          style={{
            left: dalle.left2 + dalle.width2 - 35,
            top: dalle.top2 + dalle.height2 + 10,
          }}>
          저장
        </ImgSaveBtn>
        <img
          src={image}
          style={{
            width: dalle.width2,
            height: dalle.height2,
            left: dalle.left2 + 1,
            top: dalle.top2 + 1,
            rotate: `${dalle.rotate2}deg`,
            position: 'absolute',
          }}
          alt="Selected Sticker"
        />
        <ResizableRect
          style={{ zIndex: 1000 }}
          left={dalle.left2}
          top={dalle.top2}
          width={dalle.width2}
          height={dalle.height2}
          rotateAngle={dalle.rotate2}
          zoomable="n, w, s, e, nw, ne, se, sw"
          onRotate={handleRotate}
          onResize={handleResize}
          onDrag={handleDrag}
          onDragStop={handleDragStop}
          onResizeStop={handleResizeStop}
          onRotateStop={handleRotateStop}
        />
      </div>
    </>
  );
}

export default DalleSticker;

const CloseButton = styled.span`
  background-color: #f26c60;
  width: 2rem;
  height: 2rem;
  cursor: pointer;
  border-radius: 100%;
  position: absolute;
  top: -10px;
  right: -10px;
  z-index: 100;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0px 2px 2px 0px rgba(0, 0, 0, 0.25);
  }
`;

const ImgSaveBtn = styled.div`
  width: 3.25rem;
  height: 1.6875rem;
  flex-shrink: 0;
  border-radius: 0.3125rem;
  background: #d6b6ff;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  top: -10px; // 위치 조정
  right: -30px;
  z-index: 100;
  position: absolute;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0px 2px 2px 0px rgba(0, 0, 0, 0.25);
  }

  color: #000;
  /* font-family: Inter; */
  font-family: 'bmjua';

  font-size: 1rem;
  font-style: normal;
  font-weight: lighter;
  line-height: normal;

  .btn-no-outline {
    outline: none !important;
  }
`;
