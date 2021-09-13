import React, { useState } from "react";


const UserNameForm = ({ setUserName, isJoinee, gameName, roomId }) => {
    const [value, setValue] = useState("");
    return <form id="usernameForm" name="usernameForm">
        <div className="form-group">
            <input type="text" id="name" placeholder="Username" autoComplete="off" className="form-control" value={value}
                onChange={(e) => {
                    setValue(e.target.value);
                }} required />
        </div>
        <div className="form-group">
            <button
                type="submit"
                className="accent username-submit"
                onClick={(e) => {
                    if (isJoinee) {
                        const requestOptions = {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ userName: value })
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
                                } else if (data === "FULL"){
                                    content = "ROOM FULL!";
                                } else {
                                    content = "USERNAME ALREADY EXISTS!";
                                }
                                messageElement.innerText=content;
                            });
                    } else {
                        setUserName(value);
                    }
                    e.preventDefault();
                }}
                name="play"
                value="play">
                Play!
            </button>
        </div>
        <div id="messageDisplay"></div>
    </form>;
};
export default UserNameForm;