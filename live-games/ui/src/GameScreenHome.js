import TicTacToe from "./GameScreens/TicTacToe";
import UserNameForm from "./HomeScreen/UserNameForm";
import { useState } from "react";

const gameMap = {
    TIC_TAC_TOE: TicTacToe
};

const GameScreenHome = (props) => {
    const [userName, setUserName] = useState("");
    const GameComponent = gameMap[props.gameName] || null;
    return props.location.state || userName ?
        <GameComponent {...props} {...{ userName }} /> :
        <UserNameForm {...{
            setUserName, isJoinee: true,
            gameName: props.gameName, roomId: props.match.params.roomId
        }} />;
};

export default GameScreenHome;