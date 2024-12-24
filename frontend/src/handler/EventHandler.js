import {
  stickerActions,
  textBoxActions
} from "../stores/storeHelper.js";

const imageHandlers = {

  createSticker: (data) => {
    stickerActions.addSticker({id: data.id, image: data.image, ...data.position});
  },
  imageDrag: (data) => {
    stickerActions.updateSticker({id: data.id, ...data.position});
  },
  imageResize: (data) => {
    stickerActions.updateSticker({id: data.id, ...data.position});
  },
  imageRotate: (data) => {
    stickerActions.updateSticker({id: data.id, ...data.position});
  },
  deleteObject: (data) => {
    stickerActions.deleteSticker(data.objectId);
  },
};

const textboxHandlers  = {
  createTextBox: (data) => {
    textBoxActions.addTextBox({id: data.id, ...data.position})
  },
  textDrag:(data, textActions) => {
    textBoxActions.updateTextBox({id: data.id, ...data.position});
  },
  textDragStop:(data, textActions) => {
    textBoxActions.updateTextBox({id: data.id, ...data.position});
  },
  textResize: (data, textActions) => {
    textBoxActions.updateTextBox({id: data.id, ...data.position});
  },
  textInput: (data, textActions) => {
    textBoxActions.updateTextBox({
      id: data.id,
      content: data.content});
  },

  saveTextBox: (data, textActions) => {
    textBoxActions.updateTextBox({
      id: data.id,
      content: data.content,
      nickname: data.nickname,
      showOnly: true,
      ...data.position,
    });
  },
  deleteTextBox: (data, textActions) => {
    console.log("deleteTextId: " + data.id);
    textBoxActions.deleteTextBox(data.id)
  }
};

export const handleEvent = (data, nickname) => {
  const localNickname = localStorage.getItem('loggedInUserNickname');
  console.log('localNick: ' + localNickname + ' inputNick: ' + nickname);
  const type = data.type;
  if (type === 'textDrag') {
    if (nickname !== localNickname) {
      textboxHandlers[type](data);
    }
  } else if (textboxHandlers[type]) {
    textboxHandlers[type](data);
    console.log("data.type:", data.type);
  } else if (imageHandlers[type]) {
    imageHandlers[type](data)
  } else {
    console.log('Invalid handler type')
  }
}