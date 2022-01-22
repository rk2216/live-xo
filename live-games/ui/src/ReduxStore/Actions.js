import {createAction} from "redux-actions";

export const SET_USERNAME = 'SET_USERNAME';
export const setUserName = createAction(SET_USERNAME);

export const UPDATE_GAME = 'UPDATE_GAME';
export const updateGame = createAction(UPDATE_GAME);