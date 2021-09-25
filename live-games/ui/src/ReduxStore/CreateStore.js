import {createStore} from 'redux';
import {rootReducer} from "./RootReducer";
import Immutable from "immutable";
import {combineReducers} from "redux-immutable";

export const ROOT = 'root';
export const store = createStore(combineReducers({[ROOT]: rootReducer}),
    Immutable.fromJS({[ROOT]: {}}));