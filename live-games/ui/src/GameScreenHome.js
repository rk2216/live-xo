import TicTacToe from "./GameScreens/TicTacToe";
import UserNameForm from "./HomeScreen/UserNameForm";

const gameMap = {
    TIC_TAC_TOE: TicTacToe
};

const GameScreenHome = (props) => {
    const {userName, actions: {setUserName}} = props;
    const GameComponent = gameMap[props.gameName] || null;
    return userName ?
        <GameComponent {...props} {...{ userName }} /> :
        <UserNameForm {...{
            setUserName, isJoinee: true,
            gameName: props.gameName, roomId: props.match.params.roomId
        }} />;
};

export default GameScreenHome;