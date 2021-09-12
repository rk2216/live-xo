import React, {useState} from "react";


const UserNameForm=({setUserName})=>{
    const [value, setValue]=useState("");
    return <form id="usernameForm" name="usernameForm">
        <div className="form-group">
            <input type="text" id="name" placeholder="Username" autoComplete="off" className="form-control" value={value}
                   onChange={(e)=>{
                        setValue(e.target.value);
                    }} required/>
        </div>
        <div className="form-group">
            <button
                type="submit"
                className="accent username-submit"
                onClick={()=>{
                    setUserName(value);
                }}
                name="play"
                value="play">
                Play!
            </button>
        </div>
    </form>;
};
export default UserNameForm;