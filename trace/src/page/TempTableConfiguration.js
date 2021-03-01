import React, { useContext, useState, useEffect, useRef } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import $, { data } from 'jquery';


import {
    Form,
    Button,
    Select,
    Card,
    Row,
    Col,
    Checkbox,
    Layout,
    Avatar,
    Table,
    Alert,
    Input,
    Radio,
} from 'antd';
import Title from 'antd/lib/typography/Title';

const { Header, Content } = Layout;
const { Option } = Select;
const TempTableConfiguration = props => {
    //   console.log(props)
    const [clientid, setClientID] = useState([])
    const [ruletype, setRultType] = useState([])
    const [channeldata, setChannelData] = useState([])
    const [ClientData, setClientData] = useState([])
    const [modeData, setModeData] = useState([])
    const [channelid, setChannelID] = useState([])
    const [modeid, setModeID] = useState([])
    const [columnname, setCOlSWumnName] = useState([])
    const [fileList, setFileList] = useState([])
    const [fileListReplace, setFileListReplace] = useState([])
    const [fileListReplace1, setFileListReplace1] = useState([])

    const [tblCol, setColtblData] = useState([])
    const [tblColSw, setSWColtblData] = useState([])
    const [tblColSw1, setSWColtblDatareplace] = useState([])
    const [tblColSw2Source, setSWColtblDataSource1] = useState([])
    const [tblColSw2, setSWColtblDatareplace1] = useState([])


    const [tblColGl, setGLColtblData] = useState([])
    const [tblColGl1, setGLColtblDatareplace] = useState([])
    const [tblColGl2, setGLColtblDatareplace1] = useState([])
    const [tblColGl2Source,setGLColtblDataSource1]=useState([])

    const [tblColEJ, setEJColtblData] = useState([])
    const [tblColEJ1,setEJColtblDatareplace]=useState([])
    const [tblColEJ2,setEJColtblDatareplace1]=useState([])
    const [tblColEJ2Sourece,setEJColtblDataSource1]=useState([])



    const [tblColNPCIISS, setNPCIISSColtblData] = useState([])
    const [tblColNPCIISS1, setNPCIISSColtblDatareplace] = useState([])
    const [tblColNPCIISS2, setNPCIISSColtblDatareplace1] = useState([])
    const [tblColNPCIIIS2Source,setNPCIISSColtblDataSource1]=useState([])


    const [tblColNPCIACQ, setNPCIACQColtblData] = useState([])
    const [tblColNPCIACQ1, setNPCIACQColtblDatareplace] = useState([])
    const [tblColNPCIACQ2, setNPCIACQColtblDatareplace1] = useState([])
    const [tblColNPCIACQ2Source,setNPCIACQColtblDataSource1]=useState([])


    const [tblcoltest, setSelectedRows] = useState([])
    const [switchTempName, setNameSwitchTempTable] = useState('')
    const [NPCIISSTempName, setNameNPCIISSTempTable] = useState('')
    const [NPCIACQTempName, setNameNPCIACQTempTable] = useState('')
    const [npciacqtemptablename, setNewNPCIACQ] = useState('')
    const [ejTempName, setNameEJTempTable] = useState('')
    const [glTempName, setNameGLTempTable] = useState('')

    const [selectionTypenew, setSelectionTypenew] = useState('radio');
    const [selectionType, setSelectionType] = useState('');
    const [searchText, setSearchText] = useState('');
    const [filecheck, setCheckFilename] = useState([]);
    const [switchCheck, setCheckFilenameSwitch] = useState([]);
    const [npciacqcheck, setCheckFilenameNPCIACQ] = useState([]);
    const [glCheck, setCheckFilenameGL] = useState([])
    const [npciiisCheck, setCheckFilenameNPCIISS] = useState([])
    const [ejCheck, setCheckFilenameEJ] = useState([])
    //   const [branchdata, setBranchData] = useState([])
    const [loader, setLoader] = useState(true)
    const [gltblloader, setGLtbl] = useState(false)
    const [gltblloader1, setGLtbl1] = useState(false)
    const [gltblloader2, setGLtbl2] = useState(false)
    

    const [swtblloader, setSWtbl] = useState(false)
    const [swtblloader1, setSWtbl1] = useState(false)
    const [swtblloader2, setSWtbl2] = useState(false)

    const [ejtblloader, setEJ] = useState(false)
    const [ejtblloader1, setEJ1] = useState(false)
    const [ejtblloader2, setEJ2] = useState(false)


    const [npciiss, setNPCIISS] = useState(false)
    const [npciiss1, setNPCIISS1] = useState(false)
    const [npciiss2, setNPCIISS2] = useState(false)


    const [npciacq, setNPCIACQ] = useState(false)
    const [npciacq1, setNPCIACQ1] = useState(false)
    const [npciacq2, setNPCIACQ2] = useState(false)


    const [matchtblloader, setMatchtbl] = useState(false);
    const [matchtblNew, setMatchtblNew] = useState(false);
    const [matchtablenpciacq, setmatchtblNewNPCIACQ] = useState(false)
    const [joinCondLoader, setJoinCondLoader] = useState(false);
    const [editingKey, setEditingKey] = useState('');


    const isEditing = record => record.key === editingKey;
    console.log(tblColGl1);

    const [columnname1, setCOlumnName] = useState([])
    const [checkvalue, setValue]=useState(1);

    const [checksw,setCheckWithSW]=useState()
    const [checkgl,setCheckWithGL]=useState([])
    const [checkej,setCheckWithEj]=useState([])
    const [checknpciacq,setCheckWithNPCIACQ]=useState([])
    const [checknpciiss,setCheckWithNPCIISS]=useState([])

    const[replaceNPCIACQ,setCheckFilenameNPCIACQ2]=useState([])
    const[sourceNPCIACQ,setCheckFilenameNPCIACQ1]=useState([])
    const[sourceNPCIISS,setCheckFilenameNPCIISS1]=useState([])
    const[replaceNPCIISS,setCheckFilenameNPCIISS2]=useState([])
    const[replacegl,setCheckFilenameGL2]=useState([])
    const[replaceglNew,setCheckFilenameGL2New]=useState([])
    const[sourcegl,setCheckFilenameGL1]=useState([])
    const[sourceglNew,setCheckFilenameGL1New]=useState([])
    const[sourceej,setCheckFilenameEJ1]=useState([])
    const[replaceej,setCheckFilenameEJ2]=useState([])
    const[sourceswitch,setCheckFilenameSwitch1]=useState([])
    const[sourceswitchNew,setCheckFilenameSwitch1New]=useState([])
    
    const[replaceswitch,setCheckFilenameSwitch2]=useState([])

    console.log(sourceswitchNew);
    console.log(replacegl);

    useEffect(() => {
        //   onDisplayUserRole();
        //   onDisplayChannel();
        //   onDisplayBranch();
        onDisplayClientNameList();
    }, [])

    const onDisplayClientNameList = async () => {
        try {
            const clientNameResponse = await axios.get(`clientName`);
            console.log(clientNameResponse.data)
            setLoader(false);

            const clientNameN = clientNameResponse.data;
            console.log(clientNameN);
            const clientNameList = clientNameN.map((item, index) =>
                <Option value={item.id} key={index}>{item.clientNameList}
                </Option>
            )
            setClientData(clientNameList);

        } catch (e) {
            console.log(e)

        }
    };
    const onGetChannelDetails = async (value) => {
        try {
            let selectedclientID = value;
            alert(selectedclientID);
            const channelResponse = await axios.get(`getchanneldetails/${selectedclientID}`);
            //console.log(channelResponse.data)
            setLoader(false);

            const channelN = channelResponse.data;
            //console.log(channelN);

            const listChannel = channelN.map((item, index) => <Option value={item.channelid} key={index} label={item.channelName}>{item.channelName}</Option>)
            setChannelData(listChannel);
        } catch (e) {
            console.log(e)
        }
    };

    const ongetmatchingmodeinfo = async (value) => {
        try {
            // alert("inside mode")
            ///alert("client id"+ClientData);
            //alert("channel id"+value);
            //const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);
            const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);

            console.log(modeResponse.data);


            setLoader(false);

            const modeN = modeResponse.data;
            //console.log(modeN);
            //const branch = JSON.parse(modeN.branchName);
            //console.log(branch);
            const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            setModeData(listMode);


        } catch (e) {
            console.log(e)
        }
    };

    

    
    const onSrcRplaceClick = async () => {
        try {
            console.log(checksw);
            console.log(checkgl);
            console.log(sourceswitchNew);
            console.log(replaceglNew);
            var finalReplacesrc=checksw.concat(checkgl);
            console.log(JSON.stringify(finalReplacesrc));
            // console.log( JSON.stringify(tblcoltest));
            // //${clientid}/${channelid}/${modeid}/${ruletype}/${switchCheck
            //const tempresp = await axios.post(`gettemptable/${clientid}/${channelid}/${modeid}/${ruletype}/${switchCheck}/${tblcoltest}`);
            var jsondata;
            // console.log(checksw);
            // console.log(checkgl);
            // console.log(sourceswitch);
            // console.log(replacegl);
            
            
           //if (sourceswitch == 'SWITCH' && replacegl=='GL' ) {
                //console.log(tblcoltest);
                // console.log("sourceswitch==",sourceswitch);
                // console.log("sourcegl==",sourcegl);
                // console.log("replacegl==",replacegl);
                // console.log("replaceswitch==",replaceswitch);


                // alert('hiiii');
                 jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "sourcetbl": sourceswitchNew,
                    "replacetbl": replaceglNew,
                    "jsonsrcreplacestring":JSON.stringify(finalReplacesrc)
                }
            //}
            // // if (glCheck == 'GL') {
            //     console.log(tblcoltest);
            //     jsondata = {
            //         "clientid": clientid,
            //         "channelid": channelid,
            //         "modeid": modeid,
            //         "ruletype": ruletype,
            //         "tableNames": glCheck,
            //         "checkedCol": JSON.stringify(tblcoltest)
            //     }
            // }
            // if (npciiisCheck == 'NPCIISS') {
            //     console.log(tblcoltest);
            //     jsondata = {
            //         "clientid": clientid,
            //         "channelid": channelid,
            //         "modeid": modeid,
            //         "ruletype": ruletype,
            //         "tableNames": npciiisCheck,
            //         "checkedCol": JSON.stringify(tblcoltest)
            //     }
            // }
            // if (npciacqcheck == 'NPCIACQ') {
            //     console.log(tblcoltest);
            //     jsondata = {
            //         "clientid": clientid,
            //         "channelid": channelid,
            //         "modeid": modeid,
            //         "ruletype": ruletype,
            //         "tableNames": npciacqcheck,
            //         "checkedCol": JSON.stringify(tblcoltest)
            //     }
            // }
            // if (ejCheck == 'EJ') {
            //     console.log(tblcoltest);
            //     jsondata = {
            //         "clientid": clientid,
            //         "channelid": channelid,
            //         "modeid": modeid,
            //         "ruletype": ruletype,
            //         "tableNames": ejCheck,
            //         "checkedCol": JSON.stringify(tblcoltest)
            //     }
            // }
            var response;
            $.ajax({
                type: "POST",
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                async: false,
                url: "http://192.168.1.130:8080/Admin/api/gettempconfig",
                //url: "http://localhost:8080/Admin/api/getxmlfileformat",
                data:JSON.stringify(jsondata), // Note it is important
                success: function (result) {
                    console.log(result);
                    response = result;
                }
            });

             console.log(response);


        }
        catch (e) {
            console.log(e);
        }
    }


    const ontempselectcol = async () => {
        try {
            //${clientid}/${channelid}/${modeid}/${ruletype}/${switchCheck
            //const tempresp = await axios.post(`gettemptable/${clientid}/${channelid}/${modeid}/${ruletype}/${switchCheck}/${tblcoltest}`);
            var jsondata;
            if (switchCheck == 'SWITCH') {
                console.log(tblcoltest);
                jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "tableNames": switchCheck,
                    "checkedCol": JSON.stringify(tblcoltest)
                }
            }
            if (glCheck == 'GL') {
                console.log(tblcoltest);
                jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "tableNames": glCheck,
                    "checkedCol": JSON.stringify(tblcoltest)
                }
            }
            if (npciiisCheck == 'NPCIISS') {
                console.log(tblcoltest);
                jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "tableNames": npciiisCheck,
                    "checkedCol": JSON.stringify(tblcoltest)
                }
            }
            if (npciacqcheck == 'NPCIACQ') {
                console.log(tblcoltest);
                jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "tableNames": npciacqcheck,
                    "checkedCol": JSON.stringify(tblcoltest)
                }
            }
            if (ejCheck == 'EJ') {
                console.log(tblcoltest);
                jsondata = {
                    "clientid": clientid,
                    "channelid": channelid,
                    "modeid": modeid,
                    "ruletype": ruletype,
                    "tableNames": ejCheck,
                    "checkedCol": JSON.stringify(tblcoltest)
                }
            }
            var response;
            $.ajax({
                type: "POST",
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                async: false,
                url: "http://192.168.1.130:8080/Admin/api/gettemptable",
                //url: "http://localhost:8080/Admin/api/getxmlfileformat",
                data: JSON.stringify(jsondata), // Note it is important
                success: function (result) {
                    console.log(result);
                    response = result;
                }
            });

            console.log(response);


        }
        catch (e) {
            console.log(e);
        }
    }

    const onChange = e => {
        console.log('radio checked', e.target.value);
        setValue($(e.target.value));
        console.log('onchange for radio',checkvalue);
        if(checkvalue=='SWITCH'){
            alert('inside radio switch');
           // onChangeColumnNameSwitchReplace();
        }

      };

    const getFileList = async (value) => {
        try {
            ///alert("client id"+ClientData);

            console.log("rule id", ruletype);
            //const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);
            const fileResponse = await axios.get(`getfiletypes/${channelid}`);

            console.log(fileResponse.data);


            setLoader(false);
            var filelist = fileResponse.data;
            var result = filelist.map((item) => item.fileList);
            console.log(result);
            var splitresult = result.toString().split(',');
            console.log(splitresult);
            // var finalListFile=splitresult.map((item,index)=>
            //     <Checkbox value={item}  key={index} onChange={onChangeColumnName}>{item}</Checkbox>
            // )
            // setFileList(finalListFile);
            //    console.log("rule type",value);
            if (modeid == 3 && value == 3) {
                alert(modeid);
                alert(ruletype);
                if (splitresult.includes("SWITCH") && splitresult.includes("GL") && splitresult.includes("NPCIISS")) {
                    // alert("ej gl");
                    alert(value);
                    var finalListFile = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitch}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGL}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[2]} key={3} onChange={onChangeColumnNameNPCIISS}>{splitresult[2]}</Checkbox></div>
                    // +fileListReplace
                    // <Checkbox value={splitresult[3]}  key={2} onChange={onChangeColumnName}>{splitresult[3]}</Checkbox>
                    // // +
                //  var finalListFile1 = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitchReplace}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGLReplace}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[2]} key={3} onChange={onChangeColumnNameNPCIISSReplace}>{splitresult[2]}</Checkbox></div>
                   var finalListFile1 = <div><Radio.Group onChange={onChange}  size="large"><Radio value={splitresult[4]}  >{splitresult[4]}</Radio><Radio value={splitresult[3]}  >{splitresult[3]}</Radio><Radio value={splitresult[2]} >{splitresult[2]}</Radio></Radio.Group>                   </div>
                   var finalListFile2 = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitchReplace1}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGLReplace1}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[2]} key={3} onChange={onChangeColumnNameNPCIISSReplace1}>{splitresult[2]}</Checkbox></div>
                    //var finalListFile2 = <div><Radio value={splitresult[4]} >{splitresult[4]}</Radio><Radio value={splitresult[3]}>{splitresult[3]}</Radio><Radio value={splitresult[2]}>{splitresult[2]}</Radio></div>

                    // <Checkbox value={splitresult[2]}  key={3} onChange={onChangeColumnName}>{splitresult[2]}</Checkbox>;
                    console.log(finalListFile);
                    setFileList(finalListFile);
                    setFileListReplace(finalListFile1);
                    setFileListReplace1(finalListFile2);

                } else {
                    alert("else");
                }
            }
            if (modeid == 2 && value == 4) {
                alert(modeid);
                alert(ruletype);
                if (splitresult.includes("NPCIACQ") && splitresult.includes("GL") && splitresult.includes("SWITCH") && splitresult.includes("EJ")) {
                    var finalListFile = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitch}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGL}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[1]} key={3} onChange={onChangeColumnNameNPCIACQ}>{splitresult[1]}</Checkbox><Checkbox value={splitresult[5]} key={4} onChange={onChangeColumnNameEJ}>{splitresult[5]}</Checkbox></div>
                    setFileList(finalListFile);
                    var finalListFile1 = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitchReplace}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGLReplace}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[1]} key={3} onChange={onChangeColumnNameNPCIACQReplace}>{splitresult[1]}</Checkbox><Checkbox value={splitresult[5]} key={4} onChange={onChangeColumnNameEJReplace}>{splitresult[5]}</Checkbox></div>
                    setFileListReplace(finalListFile1);
                    var finalListFile2 = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitchReplace1}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGLReplace1}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[1]} key={3} onChange={onChangeColumnNameNPCIACQReplace1}>{splitresult[1]}</Checkbox><Checkbox value={splitresult[5]} key={4} onChange={onChangeColumnNameEJReplace1}>{splitresult[5]}</Checkbox></div>
                    setFileListReplace1(finalListFile2);

                }
                else {
                    alert("else");
                }
            }
            // const modeN = modeResponse.data;
            // //console.log(modeN);
            // //const branch = JSON.parse(modeN.branchName);
            // //console.log(branch);
            // const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            // setModeData(listMode);

        } catch (e) {
            console.log(e)
        }
    };


    const getFileDataCol = async (value) => {
        try {

            const fileResponse = await axios.get(`getFileDataCol1/${value}`);
            console.log(fileResponse.data)
            const colResult = fileResponse.data;

            setLoader(false);
            if (value == "SWITCH") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setSWColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'source',
                    tblname:'SWITCH',
                    key: index
                }));
                setSWColtblDatareplace(dataAll1);
                setSWColtblDataSource1(dataAll1);

                const tblNameSwitch = colResult.map((item, index) => item.fileName);
                console.log(tblNameSwitch);
                setNameSwitchTempTable(tblNameSwitch[0]);
                alert(tblNameSwitch[0])
            }

            if (value == "GL") {
                // console.log(tblcoltest);
                //     const columnNamess = colResult[0];
                //   //  console.log(columnNamess);
                //     const tempFileName = colResult[1]
                //console.log(tempFileName)
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setGLColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'source',
                    tblname:'GL',
                    key: index
                }));
                setGLColtblDatareplace(dataAll1);
                setGLColtblDataSource1(dataAll1);

                const tblNameGL = colResult.map((item, index) => item.tableName);
                setNameGLTempTable(tblNameGL[0]);

                // console.log(tblcoltest);
            }

            if (value == "NPCIISS") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setNPCIISSColtblData(dataAll);
                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'source',
                    key: index
                }));
                setNPCIISSColtblDatareplace(dataAll1);
                setNPCIISSColtblDataSource1(dataAll1);


                const tblNameNPCIISS = colResult.map((item, index) => item.tableName);
                setNameNPCIISSTempTable(tblNameNPCIISS[0]);
                console.log(tblcoltest);
            }

            if (value == "NPCIACQ") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setNPCIACQColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'source',
                    key: index

                }));
                setNPCIACQColtblDatareplace(dataAll1);
                setNPCIACQColtblDataSource1(dataAll1);


                const tblNameNPCIACQ = colResult.map((item, index) => item.tableName);
                setNameNPCIACQTempTable(tblNameNPCIACQ[0]);

            }

            if (value == "EJ") {
                // alert("in ej ");
                // console.log(NPCIACQTempName);
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setEJColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'source',
                    key: index
                }));
                setEJColtblDatareplace(dataAll1);
                setEJColtblDataSource1(dataAll1);


                const tblNameEJ = colResult.map((item, index) => item.tableName);
                setNameEJTempTable(tblNameEJ[0]);
                console.log(tblcoltest);
            }

            //const modeN = modeResponse.data;
            //console.log(modeN);
            //const branch = JSON.parse(modeN.branchName);
            //console.log(branch);
            //const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            //setModeData(listMode);

        } catch (e) {
            console.log(e)
        }
    };


    const getFileDataCol1 = async (value) => {
        try {

            const fileResponse = await axios.get(`getFileDataCol1/${value}`);
            console.log(fileResponse.data)
            const colResult = fileResponse.data;

            setLoader(false);
            if (value == "SWITCH") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setSWColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'replace',
                    tblname:'SWITCH',
                    key: index
                }));
                setSWColtblDatareplace(dataAll1);
                setSWColtblDatareplace1(dataAll1);

                const tblNameSwitch = colResult.map((item, index) => item.fileName);
                console.log(tblNameSwitch);
                setNameSwitchTempTable(tblNameSwitch[0]);
                alert(tblNameSwitch[0])
            }

            if (value == "GL") {
                
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setGLColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'replace',
                    tblname:'GL',
                    key: index
                }));
                setGLColtblDatareplace(dataAll1);
                setGLColtblDatareplace1(dataAll1);

                const tblNameGL = colResult.map((item, index) => item.tableName);
                setNameGLTempTable(tblNameGL[0]);

                // console.log(tblcoltest);
            }

            if (value == "NPCIISS") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setNPCIISSColtblData(dataAll);
                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'replace',
                    key: index
                }));
                setNPCIISSColtblDatareplace(dataAll1);
                setNPCIISSColtblDatareplace1(dataAll1);


                const tblNameNPCIISS = colResult.map((item, index) => item.tableName);
                setNameNPCIISSTempTable(tblNameNPCIISS[0]);
                console.log(tblcoltest);
            }

            if (value == "NPCIACQ") {
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setNPCIACQColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'replace',
                    key: index

                }));
                setNPCIACQColtblDatareplace(dataAll1);
                setNPCIACQColtblDatareplace1(dataAll1);


                const tblNameNPCIACQ = colResult.map((item, index) => item.tableName);
                setNameNPCIACQTempTable(tblNameNPCIACQ[0]);

            }

            if (value == "EJ") {
                // alert("in ej ");
                // console.log(NPCIACQTempName);
                const dataAll = colResult.map((item, index) => ({
                    colNameNo: item.columnNameInNumber,
                    colNameString: item.columnNameInString,
                    chosen: true,
                    key: index
                }));
                setEJColtblData(dataAll);

                const dataAll1 = colResult.map((item, index) => ({
                    colNameString: item.columnNameInString,
                    tabletype:'replace',
                    key: index
                }));
                setEJColtblDatareplace(dataAll1);
                setEJColtblDatareplace1(dataAll1);


                const tblNameEJ = colResult.map((item, index) => item.tableName);
                setNameEJTempTable(tblNameEJ[0]);
                console.log(tblcoltest);
            }

            //const modeN = modeResponse.data;
            //console.log(modeN);
            //const branch = JSON.parse(modeN.branchName);
            //console.log(branch);
            //const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            //setModeData(listMode);

        } catch (e) {
            console.log(e)
        }
    };

    const menuData = props.location.state;
    //console.log(menuData);

    function onChangeColumnNameSwitch(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameSwitch(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'SWITCH') {
            getFileDataCol(filelistCheck);
            setSWtbl(true);
            setGLtbl(false);
            // setMatchtblNew(false);
        }
    }
    function onChangeColumnNameSwitchReplace(e) {
    alert("this is source switch");
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameSwitch1(filelistCheck);
        setCheckFilenameSwitch1New(filelistCheck);

        

       
        if (filelistCheck == 'SWITCH') {
            alert("onChangeColumnNameSwitchReplace");
            getFileDataCol(filelistCheck);
            setSWtbl1(true);
            setGLtbl1(false);
            // setMatchtblNew(false);
        }
    }
   
    function onChangeColumnNameSwitchReplace1(e) {
        alert('onChangeColumnNameSwitchReplace1');
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameSwitch2(`${e.target.value}`);
        alert(filelistCheck);
        if (filelistCheck == 'SWITCH') {
            alert("filelistCheck===",filelistCheck);
            getFileDataCol1(filelistCheck);
            setSWtbl2(true);
            setGLtbl2(false);
            setNPCIISS2(false);
            setEJ2(false);
            // setMatchtblNew(false);
        }
    }
   
    function onChangeColumnNameNPCIACQ(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIACQ(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIACQ') {
            getFileDataCol(filelistCheck);
            setNPCIACQ(true);
            setSWtbl(false);
            setGLtbl(false);
            //   // setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
        }
    }
    function onChangeColumnNameNPCIACQReplace(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIACQ1(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIACQ') {
            alert("onChangeColumnNameNPCIACQReplace");
            getFileDataCol(filelistCheck);
            setNPCIACQ1(true);
            setSWtbl1(false);
            setGLtbl1(false);
            //setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
        }
    }

    function onChangeColumnNameNPCIACQReplace1(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIACQ2(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIACQ') {
            alert('onChangeColumnNameNPCIACQReplace1')
            getFileDataCol1(filelistCheck);
            setNPCIACQ2(true);
            setSWtbl2(false);
            setGLtbl2(false);
            setNPCIISS2(false);
            //setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
        }
    }

    function onChangeColumnNameEJ(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameEJ(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'EJ') {
            getFileDataCol(filelistCheck);
            setEJ(true);
            setSWtbl(false);
            setGLtbl(false);
            setNPCIACQ(false);
            //   // setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
            //   setCheckFilenameNPCIACQ('');
        }
    }

    function onChangeColumnNameEJReplace(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameEJ1(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'EJ') {
            getFileDataCol(filelistCheck);
            setEJ1(true);
            setSWtbl1(false);
            setGLtbl1(false);
            setNPCIACQ1(false);

            //   // setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
            //   setCheckFilenameNPCIACQ('');
        }
    }

    function onChangeColumnNameEJReplace1(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameEJ2(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'EJ') {
            getFileDataCol1(filelistCheck);
            setEJ2(true);
            setSWtbl2(false);
            setGLtbl2(false);
            setNPCIACQ2(false);

            //   // setMatchtblNew(false);
            //   setCheckFilenameSwitch('');
            //   setCheckFilenameGL('');
            //   setCheckFilenameNPCIACQ('');
        }
    }

    function onChangeColumnNameGL(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameGL(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'GL') {

            getFileDataCol(filelistCheck);
            setGLtbl(true);
            setSWtbl(false);
            //   setMatchtblNew(false);
        }
    }

    function onChangeColumnNameGLReplace(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameGL1(filelistCheck);
        setCheckFilenameGL1New(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'GL') {
            alert("onChangeColumnNameGLReplace");
            getFileDataCol(filelistCheck);
            setGLtbl1(true);
            setSWtbl1(false);
            setNPCIACQ1(false);
            setNPCIISS1(false);

            //   setMatchtblNew(false);
        }
    }

    function onChangeColumnNameGLReplace1(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameGL2(filelistCheck);
        setCheckFilenameGL2New(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'GL') {
            alert("onChangeColumnNameGLReplace1");
            getFileDataCol1(filelistCheck);
            setGLtbl2(true);
            setSWtbl2(false);
            setNPCIACQ2(false);
            setNPCIISS2(false);

            //   setMatchtblNew(false);
        }
    }
    function onChangeColumnNameNPCIISS(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIISS(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIISS') {
            getFileDataCol(filelistCheck);
            setNPCIISS(true);
            //   setCheckFilenameGL('');
            //   setCheckFilenameSwitch('');

        }
    }
    function onChangeColumnNameNPCIISSReplace(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIISS1(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIISS') {
            getFileDataCol(filelistCheck);
            setNPCIISS1(true);
            //   setCheckFilenameGL('');
            //   setCheckFilenameSwitch('');

        }
    }
    function onChangeColumnNameNPCIISSReplace1(e) {
        console.log(`checked = ${e.target.value}`);
        const filelistCheck = `${e.target.value}`;
        setCheckFilenameNPCIISS2(filelistCheck);
        alert(filelistCheck);
        if (filelistCheck == 'NPCIISS') {
            getFileDataCol1(filelistCheck);
            setNPCIISS2(true);
            setEJ2(false);
            setGLtbl2(false);
            setSWtbl2(false);
            //   setCheckFilenameGL('');
            //   setCheckFilenameSwitch('');

        }
    }

    function onChangeReconType(value) {
        console.log(`selected ${value}`);
        setRultType(value);
        getFileList(value);

        //ongetMatchingRuleSetForClient(value);
    }



    function onChangeTxnMode(value) {
        console.log(`selected ${value}`);
        setModeID(value);

    }
    function onChangeChanneltName(value) {
        console.log(`selected ${value}`);
        setChannelID(value);
        ongetmatchingmodeinfo(value);
    }

    function handleChange(value) {
        console.log(`selected ${value}`);
    }

    const rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {


           console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
           setSelectedRows(selectedRows);

              if (sourceswitch == 'SWITCH') {
                  alert("##switch##");
                checkWithSW(selectedRows);
                console.log(selectedRows);
                setCheckFilenameSwitch1('');


              }
            //   if (replaceswitch == 'SWITCH' ) {
            //     alert("replace switch");
            //     checkWithSW(selectedRows);
            //   }
            //   if (sourcegl == 'GL') {
            //     alert("source gl");
            //     checkWithGL(selectedRows);
            //   }
              if (replacegl == 'GL') {
                alert("##GL##");
                console.log(selectedRows);
                checkWithGL(selectedRows);

              }
            //   if (sourceej == 'EJ') {
            //     checkWithEj(selectedRows);
            //   }
            //   if (replaceej == 'EJ') {
            //     checkWithEj(selectedRows);
            //   }
            //   if (sourceNPCIACQ == 'NPCIACQ') {
            //     checkWithNPCIACQ(selectedRows);
            //   }
            //   if (replaceNPCIACQ == 'NPCIACQ') {
            //     checkWithNPCIACQ(selectedRows);
            //   }
            //   if (sourceNPCIISS == 'NPCIISS') {
            //     checkWithNPCIISS(selectedRows);
            //   }
            //   if (replaceNPCIISS == 'NPCIISS') {
            //     checkWithNPCIISS(selectedRows);
            //   }
            //   console.log(columnnamematch);
            //   if (/*columnnamematch=='GLCBSTEMP = SWITCHTEMP'*/ npciiisCheck == 'NPCIISS') {
            //     checkMatchtableWithNPCI(selectedList);
            //   }

            //   if (npciacqcheck == 'NPCIACQ') {
            //     checkMatchtableWithNPCIACQ(selectedList);
            //   }

            //   if (ejCheck == 'EJ') {
            //     checkMatchtableWithEJ(selectedList);
            //   }

        },

        // getCheckboxProps(record) {
        //  // console.log(record.colName);
        //   return {
        //     props: {
        //       name: !record.colName ? 'disabled' : '',
        //       //disabled: record.isDisabled || !record.colName
        //     }
        //   }
        // }

    };

    const checkWithSW = (selectedList) => {
        alert("inside switch & gl");
        console.log(selectedList);
        //var sw=JSON.stringify(selectedList);
        setCheckWithSW(selectedList);
        //setCheckFilenameSwitch1('');
        //setCheckFilenameSwitch2('');
        

    }

    const checkWithEj=(selectedList)=>{

        console.log(selectedList);
        setCheckWithEj(selectedList);

    }


    const checkWithNPCIACQ=(selectedList)=>{
        console.log(selectedList);
        setCheckWithNPCIACQ(selectedList);

    }
    const checkWithNPCIISS=(selectedList)=>{
        console.log(selectedList);
        setCheckWithNPCIISS(selectedList);

    }
    const checkWithGL = (selectedList) => {
        alert("inside gl");

          console.log(selectedList);
          setCheckWithGL(selectedList);
          setCheckFilenameGL1('');
          setCheckFilenameGL2('');
        
      }

    //console.log(selectionType);
    // debugger;
    const columnsGL = [
        {
            title: 'GL Column Name Number',
            dataIndex: 'colNameNo',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'GL Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        }
    ];

    const columnsGLReplace = [
        {
            title: 'GL Column Name Number',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',
        },
        {
            title: 'Table Type',
            dataIndex: 'tabletype',
            key: 'index',
        },
        {
            title: 'Table Name',
            dataIndex: 'tblname',
            key: 'index',
        }
    ];

    const columnssw = [
        {
            title: 'SWITCH Column Name Number',
            dataIndex: 'colNameNo',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'SWITCH Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        }
    ];

    const columnsswreplace = [
        {
            title: 'SWITCH Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'Table Type',
            dataIndex: 'tabletype',
            key: 'index',
        },
        {
            title: 'Table Name',
            dataIndex: 'tblname',
            key: 'index',
        }
    ];

    const columnsnpciiss = [
        {
            title: 'NPCI Column Name Number',
            dataIndex: 'colNameNo',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'NPCI Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        }
    ];

    const columnsnpciissreplace = [
        {
            title: 'NPCI Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'Table Type',
            dataIndex: 'tabletype',
            key: 'index',
        }
    ];

    const columnsnpciacq = [
        {
            title: 'NPCI Column Name Number',
            dataIndex: 'colNameNo',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'NPCI Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        }
    ];

    const columnsnpciacqreplace = [
        {
            title: 'NPCI Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'Table Type',
            dataIndex: 'tabletype',
            key: 'index',
        }
    ];
    const columnsEJ = [
        {
            title: 'EJ Column Name Number',
            dataIndex: 'colNameNo',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'EJ Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        }
    ];

    const columnsEJReplace = [
        {
            title: 'EJ Column Name String',
            dataIndex: 'colNameString',
            key: 'index',
            //   width: '5%',

        },
        {
            title: 'Table Type',
            dataIndex: 'tabletype',
            key: 'index',
        }

    ];

    function onChangeColumnName(checkedValues) {
        console.log('checked = ', checkedValues);
        setCOlumnName(checkedValues);
    }



    const [form] = Form.useForm()

    const getinfofromjointables = async () => {

        try {
            const validateFields = await form.validateFields();
            const values = form.getFieldsValue();
            console.log(values);
            //@PostMapping("getinfofromjointables/{clientid}/{channelid}/{tmode}/{recontype}/{fileNameList}/{colNameList}")
            const response = await axios.post(`getinfofromjointables/${clientid}/${channelid}/${modeid}/${ruletype}/${columnname}/${selectionType}`);
            console.log(response.data);

            // if(JSON.stringify(response.data) === 'Save')
            // {
            //   alert("user added successfully");
            // }
            // else{
            //   alert("already exist");
            // }
            //  //props.history.push("/AddUser",response.data)
        } catch (e) {
            console.log(e)
        }
    }


    // function onChange(checkedValues) {
    //   console.log('checked = ', checkedValues);
    // }
    function onChangeClientName(value) {
        console.log(`selected ${value}`);
        setClientID(value)
        onGetChannelDetails(value);

    }
    return (

        <Layout>
            <Header style={{ padding: "20px" }}>
                <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
                <Title
                    level={3} style={{ color: "white" }}>Trace</Title>
            </Header>
            <Layout>
                <MenuSideBar menuData={menuData} />
                <Layout style={{ height: "100vh", backgroundColor: "white" }}>
                    <Content>
                        <Row gutter={[16]}>
                            <Col span={12} >
                                <Card title="Temp Table Configuration" bordered={true} style={{ width: 900 }} >
                                    <b>
                                    <Form initialValues={{ remember: true }} layout={"vertical"} form={form} size={"large"}>
                                        <Row gutter={[16, 16]}>
                                            <Col span={5}>
                                                <Form.Item label="Client Name" name="clientId" >
                                                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                                                        {ClientData}
                                                    </Select>
                                                </Form.Item>

                                            </Col>
                                            <Col span={5}>
                                                <Form.Item label="Channel Type" name="ChannelType" >
                                                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeChanneltName}>
                                                        {channeldata}
                                                    </Select>
                                                </Form.Item>
                                            </Col>
                                            <Col span={5}>
                                                <Form.Item label="Mode Type" name="ModeType">
                                                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeTxnMode}>
                                                        {modeData}
                                                    </Select>
                                                </Form.Item>
                                            </Col>
                                            <Col span={5} >
                                                <Form.Item label="Recon Type" name="ReconType" >
                                                    <Select style={{ width: 150 }} onChange={onChangeReconType}>
                                                        <Option value="2">2-way</Option>
                                                        <Option value="3">3-way</Option>
                                                        <Option value="4">4-way</Option>
                                                    </Select>
                                                </Form.Item>
                                            </Col>
                                        </Row>

                                        <Row>

                                            {fileList}

                                        </Row>

                                        <br></br>
                                        <Row>
                                            <Form.Item>
                                                <Button onClick={ontempselectcol}>Submit</Button>
                                                <Button style={{ margin: '0 8px' }} onClick={props.history.goBack} >Back</Button>
                                            </Form.Item>
                                        </Row>

                                        <Col span={6} >

                                        </Col>
                                        <Row>
                                            <Col span={6}>

                                                {swtblloader ? (
                                                    <Table dataSource={tblColSw} columns={columnssw} rowSelection={{
                                                        type: selectionType,
                                                        ...rowSelection,
                                                    }}
                                                        pagination={false}
                                                        bordered
                                                        scroll={{ y: 400 }}
                                                        style={{ width: 400 }}
                                                    />
                                                ) : (" ")}
                                                {gltblloader ? (
                                                    <Table dataSource={tblColGl} columns={columnsGL} rowSelection={{
                                                        type: selectionType,
                                                        ...rowSelection,
                                                    }}
                                                        pagination={false}
                                                        bordered
                                                        scroll={{ y: 400 }}
                                                        style={{ width: 400 }}
                                                    />
                                                ) : (" ")}


                                                {npciiss ? (
                                                    <Table dataSource={tblColNPCIISS} columns={columnsnpciacq} rowSelection={{
                                                        type: selectionType,
                                                        ...rowSelection,
                                                    }}
                                                        pagination={false}
                                                        bordered
                                                        scroll={{ y: 400 }}
                                                        style={{ width: 400 }}
                                                    />
                                                ) : (" ")}

                                                {npciacq ? (
                                                    <Table dataSource={tblColNPCIACQ} columns={columnsnpciiss} rowSelection={{
                                                        type: selectionType,
                                                        ...rowSelection,
                                                    }}
                                                        pagination={false}
                                                        bordered
                                                        scroll={{ y: 400 }}
                                                        style={{ width: 400 }}
                                                    />
                                                ) : (" ")}
                                                {
                                                ejtblloader ? (
                                                    <Table dataSource={tblColEJ} columns={columnsEJ} rowSelection={{
                                                        type: selectionType,
                                                        ...rowSelection,
                                                    }}
                                                        pagination={false}
                                                        bordered
                                                        scroll={{ y: 400 }}
                                                        style={{ width: 400 }}
                                                    />
                                                ) : (" ")}
                                            </Col>
                                        </Row>
                                    </Form>
                                    </b>
                                </Card>
                        
                            </Col>
                            <Col span={6}>
                                <b>
                                <Card title=" Configuration" size="default" bordered={true} style={{ width: 900 }} >
                                    <Row gutter={[8, 8]}>
                                        <Col span={12} >
                                            Source Table
                                            {fileListReplace}
                                            <br></br>
                                            {swtblloader1 ? (
                                                <Table dataSource={tblColSw2Source} columns={columnsswreplace} rowSelection={{
                                                    type: selectionTypenew,
                                                    ...rowSelection,
                                                    
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 350 }}
                                                />
                                            ) : (" ")}
                                            {gltblloader1 ? (
                                                <Table dataSource={tblColGl2Source} columns={columnsGLReplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {npciiss1 ? (
                                                <Table dataSource={tblColNPCIIIS2Source} columns={columnsnpciissreplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {npciacq1 ? (
                                                <Table dataSource={tblColNPCIACQ2Source} columns={columnsnpciacqreplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {ejtblloader1 ? (
                                                <Table dataSource={tblColEJ2Sourece} columns={columnsEJReplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                        </Col>
                                        <Col span={12} >
                                            Replace Table
                                            <br></br>
                                            {fileListReplace1}
                                            
                                            <br></br>
                                            {swtblloader2 ? (
                                                <Table dataSource={tblColSw2} columns={columnsswreplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {gltblloader2 ? (
                                                <Table dataSource={tblColGl2} columns={columnsGLReplace} rowSelection={{
                                                    type: selectionTypenew,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 350 }}
                                                />
                                            ) : (" ")}
                                            {npciiss2 ? (
                                                <Table dataSource={tblColNPCIISS2} columns={columnsnpciissreplace} rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {npciacq2 ? (
                                                <Table 
                                                    dataSource={tblColNPCIACQ2} 
                                                    columns={columnsnpciacqreplace} 
                                                    rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                            {ejtblloader2 ? (
                                                <Table 
                                                    dataSource={tblColEJ2} 
                                                    columns={columnsEJReplace} 
                                                    rowSelection={{
                                                    type: selectionType,
                                                    ...rowSelection,
                                                }}
                                                    pagination={false}
                                                    bordered
                                                    scroll={{ y: 400 }}
                                                    style={{ width: 300 }}
                                                />
                                            ) : (" ")}
                                        </Col>
                                    </Row>
                                    <br></br>
                                        <Row>
                                            <Form.Item>
                                                <Button onClick={onSrcRplaceClick} >Submit</Button>
                                                <Button style={{ margin: '0 8px' }} onClick={props.history.goBack} >Back</Button>
                                            </Form.Item>
                                        </Row>
                                </Card>
                                </b>
                            </Col>
                        </Row>
                    </Content>
                </Layout>
            </Layout >
        </Layout >
    );
};
export default TempTableConfiguration;