import UserNameForm from "./UserNameForm";
import {useState} from "react";
import GameList from "./GameList";

const HomeScreen=()=>{
    const [userName, setUserName]=useState(null);

    return !userName ? <UserNameForm {...{setUserName}}/> : <GameList {...{userName}}/>;
};
export default HomeScreen;