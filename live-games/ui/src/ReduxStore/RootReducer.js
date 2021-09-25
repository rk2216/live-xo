import Immutable from "immutable";
import {SET_USERNAME} from "./Actions";
import {USERNAME} from "./Constants";

export const rootReducer = (state = Immutable.Map(), action) => {
  switch (action.type) {
    case SET_USERNAME:
      return state.set(USERNAME, action.payload);
    default:
      return state
  }
};