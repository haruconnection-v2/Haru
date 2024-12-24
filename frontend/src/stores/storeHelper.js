import useStickerStore from "./stickerStore.js";
import useTextStore from "./textStore.js";

export const stickerActions = {
  addSticker: (data) => useStickerStore.getState().addSticker(data),
  updateSticker: (data) => useStickerStore.getState().updateSticker(data),
  deleteSticker: (id) => useStickerStore.getState().deleteSticker(id),
};

export const textBoxActions = {
  addTextBox: (data) => useTextStore.getState().addText(data),
  updateTextBox: (data) => useTextStore.getState().updateText(data),
  deleteTextBox: (id) => useTextStore.getState().deleteText(id),
};