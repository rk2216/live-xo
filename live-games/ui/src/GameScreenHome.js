import React from 'react';
import GameComponent from "./GameScreens/GameComponent";
import UserNameForm from "./HomeScreen/UserNameForm";

const GameScreenHome = (props) => {
    const { userName, actions: { setUserName } } = props;
    return userName ?
        <GameComponent {...props} {...{ userName }} /> :
        <UserNameForm {...{
            setUserName, isJoinee: true,
            gameName: props.gameName, roomId: props.match.params.roomId
        }} />;
};

export default GameScreenHome;  