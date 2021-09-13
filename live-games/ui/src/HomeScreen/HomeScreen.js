import UserNameForm from "../UserNameForm";
import {useState} from "react";
import GameList from "../GameList";

const HomeScreen=()=>{
    const [userName, setUserName]=useState(null);
    const [isHost, setIsHost]=useState(false);

    return !userName ? <UserNameForm {...{setUserName}}/> : <GameList {...{userName}}/>;
};
export default HomeScreen;