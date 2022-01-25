import {ROOT} from "../ReduxStore/CreateStore";
import {USERNAME} from "../ReduxStore/Constants";

export const getUserName = state => state.getIn([ROOT, USERNAME]) || '';