import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import GameScreenHome from "./GameScreenHome";
import {setUserName, updateGame} from "./ReduxStore/Actions";
import {getUserName} from "./HomeScreen/Selectors";

const mapStateToProps = (state) => {
    return {
        userName: getUserName(state)
    }
};

const mapDispatchToProps = (dispatch) => ({
    actions: bindActionCreators({setUserName, updateGame}, dispatch)
});


export default connect(mapStateToProps, mapDispatchToProps)(GameScreenHome);