import React from 'react'
import { Switch, Router, Route, withRouter } from 'react-router-dom'
import Login from '../page/login'
import Dashboard from '../page/dasboard';
import UserDetails from '../page/UserDetails';
import { Layout, Modal } from 'antd';
import MenuSideBar from '../page/menuSideBar';
import AddUser from '../page/AddUser';
import RoleDetails from '../page/RoleDetails';
import ImportFile from '../page/ImportFile';
import AccessRight from '../page/AccessRight';
import CurrencyRegistration from '../page/CurrencyRegistration';
import VendorRegistration from '../page/VendorRegistration';
import ClientRegistration from '../page/ClientRegistration';
import TerminalBranchUpload from '../page/TerminalBranchUpload';
import AddClient from '../page/AddClient';
import Colorpicker from '../page/colorpicker';
import SketchExample from '../page/SketchExample';
import UpdateClient from '../page/UpdateClient';
import { MyPicker } from '../page/MyPicker';
import EditableTable from '../page/Editable';
import ChangePassword from '../page/ChangePassword';
import FileConfiguration from '../page/FileConfiguration';
import FieldIdentificationConfiguration from '../page/FieldIdentificationConfiguration';
import RunRecon from '../page/RunRecon';
import MatchingRuleConfiguration from '../page/MatchingRuleConfiguration';
import ForceSettlementRuleConfiguration from '../page/ForceSettlementRuleConfiguration';
import Demo from '../page/demo';
import DispenseSummaryReport from '../page/DispenseSummaryReport';
import MatchedTxnsReport from '../page/MatchedTxnsReport';
import UnmatchedTxnsReport from '../page/UnmatchedTxnsReport';
import ReversalTxnsReport from '../page/ReversalTxnsReport';
import JoinRuleConfiguration from '../page/JoinRuleConfiguration';
import TempTableConfiguration from '../page/TempTableConfiguration';
const AppRouter = props => {

    return (
        <Switch>
            <Route excat path="/login" component={Login} />
            <Route path="/dashboard" component={Dashboard} />
            <Route path='/UserDetails' component={UserDetails}/>
            <Route path='/addUser'component={AddUser}/>
            <Route path='/RoleDetails'component={RoleDetails}/>
            <Route path='/ImportFile' component={ImportFile}/>
            <Route path='/AccessRight' component={AccessRight}/>
            <Route path="/CurrencyRegistration" component={CurrencyRegistration} />
            <Route path="/VendorRegistration" component={VendorRegistration} />
            <Route path="/ClientRegistration" component={ClientRegistration}/>
            <Route path="/AddClient" component={AddClient}/>
            <Route path="/colorpicker" component={Colorpicker}/>
            <Route path="/SketchExample" component={SketchExample} ></Route>
            <Route path="/TerminalBranchUpload" component={TerminalBranchUpload}/>
            <Route path="/UpdateClient" component={UpdateClient}/>
            <Route path="/MyPicker" component={MyPicker}></Route>
            <Route path="/Editable" component={EditableTable}/>
            <Route path="/ChangePassword" component={ChangePassword}/>
            <Route path="/FileConfiguration" component={FileConfiguration}/>
            <Route path="/FieldIdentificationConfiguration" component={FieldIdentificationConfiguration}/>
            <Route path="/RunRecon" component={RunRecon} />
            <Route path="/MatchingRuleConfiguration" component={MatchingRuleConfiguration} />
            <Route path="/ForceSettlementRuleConfiguration" component={ForceSettlementRuleConfiguration} />
            <Route path="/demo" component={Demo}></Route>
            <Route path="/DispenseSummaryReport" component={DispenseSummaryReport}/>
            <Route path="/MatchedTxnsReport" component={MatchedTxnsReport}/>
            <Route path="/UnmatchedTxnsReport" component={UnmatchedTxnsReport}/>
            <Route path="/ReversalTxnsReport" component={ReversalTxnsReport}/>
            <Route path="/JoinRuleConfiguration" component={JoinRuleConfiguration}/>
            <Route path="/TempTableConfiguration" component={TempTableConfiguration}></Route>

        </Switch>
)
}
export default  AppRouter;