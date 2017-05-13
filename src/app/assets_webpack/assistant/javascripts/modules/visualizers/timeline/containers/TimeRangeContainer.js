import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import { createStructuredSelector } from "reselect";
import {TimeRange} from "../models";
import moment from 'moment'

import { setSelectTime, getSelectedTimeReset, timeRangeSelector } from '../ducks/timeRange'

import Button from "../../../../components/Button";

class TimeRangeContainer extends Component {
    static propTypes = {
        dispatch: PropTypes.func.isRequired,
        timeRange: PropTypes.instanceOf(TimeRange).isRequired
    };


    componentWillUnmount(){
        const { dispatch } = this.props;
        dispatch(getSelectedTimeReset());
    }

    setTimeRange(){
        const { dispatch } = this.props;

        var begin = document.getElementsByName("begin_input")[0].value;
        var end = document.getElementsByName("end_input")[0].value;

        dispatch(setSelectTime(new Date(begin), new Date(end)));
    }

    render() {
        const { timeRange } = this.props;

        var beginString = moment(timeRange.begin).format('YYYY-MM-DD');
        var endString = moment(timeRange.end).format('YYYY-MM-DD');

        return <div>
            <table>
                <tbody>
                <tr>
                    <td>Time Range from </td>
                    <td><input name="begin_input" type="date" defaultValue={beginString} readOnly={false}/></td>
                    <td>to</td>
                    <td><input name="end_input" type="date" defaultValue={endString} readOnly={false}/></td>
                    <td>
                        <Button raised={true}
                                onTouchTap={()=>this.setTimeRange()}
                                disabled={false}
                                label="LOAD"
                        />

                    </td>
                    <td>
                        <Button raised={true}
                                onTouchTap={getSelectedTimeReset}
                                disabled={false}
                                label="RESET"
                        />

                    </td>
                </tr>
                </tbody>
            </table>
        </div>

    }
}

const selector = createStructuredSelector({
    timeRange: timeRangeSelector
});

export default connect(selector)(TimeRangeContainer);
