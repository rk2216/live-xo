import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import { updateGame } from "../ReduxStore/Actions";
import { getGameState, getCurrentTurn, getPlayerPosition, getWinner } from "./Selectors";
import TicTacToe from "./TicTacToe";

const mapStateToProps = (state) => {
    return {
        gameState: getGameState(state),
        currentTurn: getCurrentTurn(state),
        playerPosition: getPlayerPosition(state),
        winner: getWinner(state)
    }
};

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators({ updateGame }, dispatch)
});


export default connect(mapStateToProps, mapDispatchToProps)(TicTacToe);