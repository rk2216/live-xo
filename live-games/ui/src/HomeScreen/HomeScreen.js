import UserNameForm from "../UserNameForm";
import {useState} from "react";
import GameList from "../GameList";
import TicTacToe from "../GameScreens/TicTacToe";



const func=(userName)=>{
    if(window.location.href==="http://localhost:3000/"){ //host
        // setIsHost(true);
        return <GameList {...{userName}}/>;
    }
    let startOfGameName=22, lengthOfGameName=11;
    let gameName=window.location.href.substring(startOfGameName, startOfGameName+lengthOfGameName);
    let startOfKey=startOfGameName+lengthOfGameName+1, lengthOfKey=4;
    let key = window.location.href.substring(startOfKey, startOfKey+lengthOfKey);
    if(gameName === "TIC_TAC_TOE"){
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName })
        };
        let isValidState;
        fetch("http://localhost:8080/"+gameName+"/"+key+"/validate", requestOptions)
            .then(response => response.text())
            .then(data => {
                isValidState=data;
            });
        if(isValidState==="VALID"){
            return <TicTacToe userName={userName} isHost={false}/>
        }else if(isValidState==="INVALID"){
            return <div> INVALID LINK! </div>;
        }else if(isValidState==="FULL"){
            return <div> ROOM FULL! </div>;
        }
    }
}
const HomeScreen=()=>{
    const [userName, setUserName]=useState(null);
    const [isHost, setIsHost]=useState(false);

    return !userName ? <UserNameForm {...{setUserName}}/> : func(userName);
        // {window.location.href} === "http://localhost:3000/" ? <GameList {...{userName}}/>;
};
export default HomeScreen;