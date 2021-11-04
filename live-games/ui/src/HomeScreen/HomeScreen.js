import React from 'react';
import UserNameForm from "./UserNameForm";
import GameList from "./GameList";

const HomeScreen = (props) => {
    const { userName, actions: { setUserName } } = props;

    return !userName ? <UserNameForm {...{ setUserName }} /> : <GameList {...{ userName }} />;
};
export default HomeScreen;