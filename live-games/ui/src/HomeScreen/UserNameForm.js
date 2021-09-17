import React, {useState} from "react";
import {Button, TextField} from "@mui/material";

const UserNameForm = ({setUserName, isJoinee, gameName, roomId}) => {
    const [value, setValue] = useState("");
    return <form id="usernameForm" name="usernameForm">
        <div id="formDiv">
            <h1><span id="liveLabel">Live</span> <span id="miniLabel">Mini</span> <span id="gameLabel">Games</span></h1>
            <div className="form-group">
                <TextField id="username" label="Username" variant="outlined" required onChange={(e) => {
                    setValue(e.target.value);
                }} value={value}/>
                {/*<input type="text" id="name" placeholder="Username" autoComplete="off" className="form-control" value={value}*/}
            </div>
            <div className="form-group">
                <Button
                    type="submit"
                    className="accent username-submit"
                    onClick={(e) => {
                        if (isJoinee) {
                            const requestOptions = {
                                method: 'POST',
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify({userName: value})
                            };
                            fetch("http://localhost:8080/validate/" + gameName + "/" + roomId, requestOptions)
                                .then(response => response.text())
                                .then(data => {
                                    let content = "";
                                    let messageElement = document.getElementById("messageDisplay");
                                    if (data === "VALID") {
                                        setUserName(value);
                                    } else if (data === "INVALID") {
                                        content = "INVALID LINK!";
                                    } else if (data === "FULL") {
                                        content = "ROOM FULL!";
                                    } else {
                                        content = "USERNAME ALREADY EXISTS!";
                                    }
                                    messageElement.innerText = content;
                                });
                        } else {
                            setUserName(value);
                        }
                        e.preventDefault();
                    }}
                    variant="contained"
                    color="success">
                    Play!
                </Button>
            </div>
            <div id="messageDisplay"/>
        </div>
    </form>;
};
export default UserNameForm;