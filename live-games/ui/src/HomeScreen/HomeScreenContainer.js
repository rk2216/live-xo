import HomeScreen from "./HomeScreen";
import {connect} from "react-redux";
import {getUserName} from "./Selectors";
import {bindActionCreators} from "redux";
import {setUserName} from "../ReduxStore/Actions";

const mapStateToProps = (state) => {
    return {
        userName: getUserName(state)
    }
};

const mapDispatchToProps = (dispatch) => ({
   actions: bindActionCreators({setUserName}, dispatch)
});


export default connect(mapStateToProps, mapDispatchToProps)(HomeScreen);