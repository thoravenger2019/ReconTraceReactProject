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
  const [tblCol, setColtblData] = useState([])
  const [tblColSw, setSWColtblData] = useState([])
  const [tblColGl, setGLColtblData] = useState([])
  const [tblColEJ, setEJColtblData] = useState([])
  const [tblColNPCIISS, setNPCIISSColtblData] = useState([])
  const [tblColNPCIACQ, setNPCIACQColtblData] = useState([])
  const [tblcoltest, setSelectedRows] = useState([])
  const [switchTempName, setNameSwitchTempTable] = useState('')
  const [NPCIISSTempName, setNameNPCIISSTempTable] = useState('')
  const [NPCIACQTempName, setNameNPCIACQTempTable] = useState('')
  const [npciacqtemptablename, setNewNPCIACQ] = useState('')
  const [ejTempName, setNameEJTempTable] = useState('')
  const [glTempName, setNameGLTempTable] = useState('')

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
  const [swtblloader, setSWtbl] = useState(false)
  const [ejtblloader, setEJ] = useState(false)
  const [npciiss, setNPCIISS] = useState(false)
  const [npciacq, setNPCIACQ] = useState(false)
  const [matchtblloader, setMatchtbl] = useState(false);
  const [matchtblNew, setMatchtblNew] = useState(false);
  const [matchtablenpciacq, setmatchtblNewNPCIACQ] = useState(false)
  const [joinCondLoader, setJoinCondLoader] = useState(false);
  const [editingKey, setEditingKey] = useState('');
  const isEditing = record => record.key === editingKey;

  const [columnname1, setCOlumnName] = useState([])
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
          var finalListFile = <div><Checkbox value={splitresult[4]} key={1} onChange={onChangeColumnNameSwitch}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]} key={2} onChange={onChangeColumnNameGL}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[2]} key={3} onChange={onChangeColumnNameNPCIISS}>{splitresult[2]}</Checkbox></div>
          // +
          // <Checkbox value={splitresult[3]}  key={2} onChange={onChangeColumnName}>{splitresult[3]}</Checkbox>
          // // +
          // <Checkbox value={splitresult[2]}  key={3} onChange={onChangeColumnName}>{splitresult[2]}</Checkbox>;
          console.log(finalListFile);
          setFileList(finalListFile);
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
          colNameString:item.columnNameInString,
          chosen: true,
          key: index
        }));
        setSWColtblData(dataAll);
    
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
            colNameString:item.columnNameInString,
            chosen: true,
            key: index
          }));
        setGLColtblData(dataAll);
        const tblNameGL = colResult.map((item, index) => item.tableName);
        setNameGLTempTable(tblNameGL[0]);

        // console.log(tblcoltest);
      }

    //   if (value == "NPCIISS") {
    //     //console.log(tblcoltest);
    //     const columnNamess = colResult[0];
    //     //console.log(columnNamess);
    //     const tempFileName = colResult[1]
    //     //console.log(tempFileName)
    //     const dataAll = columnNamess.map((item, index) => ({
    //       colName: item.columnName,
    //       chosen: false,
    //       key: index
    //     }));
    //     setNPCIISSColtblData(dataAll);
    //     const tblNameNPCIISS = tempFileName.map((item, index) => item.tableName);
    //     setNameNPCIISSTempTable(tblNameNPCIISS[0]);
    //     console.log(tblcoltest);
    //   }

    //   if (value == "NPCIACQ") {
    //     const columnNamess = colResult[0];
    //     console.log(columnNamess);
    //     const tempFileName = colResult[1];
    //     console.log(tempFileName);
    //     const dataAll = columnNamess.map((item, index) => ({
    //       colName: item.columnName,
    //       chosen: false,
    //       key: index
    //     }));
    //     setNPCIACQColtblData(dataAll);
    //     const tblNameNPCIACQ = tempFileName.map((item, index) => item.tableName);
    //     console.log(tblNameNPCIACQ[0]);
    //     setNameNPCIACQTempTable(tblNameNPCIACQ[0]);
    //     setNewNPCIACQ(tblNameNPCIACQ[0]);
    //     console.log(tblcoltest);
    //   }

    //   if (value == "EJ") {
    //     alert("in ej ");
    //     console.log(NPCIACQTempName);
    //     const columnNamess = colResult[0];
    //     console.log(columnNamess);
    //     const tempFileNameej = colResult[1];
    //     console.log(tempFileNameej);
    //     const dataAll = columnNamess.map((item, index) => ({
    //       colName: item.columnName,
    //       chosen: false,
    //       key: index
    //     }));
    //     setEJColtblData(dataAll);
    //     const tblNameEJ = tempFileNameej.map((item, index) => item.tableName);
    //     setNameEJTempTable(tblNameEJ[0]);
    //     console.log(tblcoltest);
    //   }

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
    //   setNPCIACQ(true);
    //   // setMatchtblNew(false);
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
    //   setEJ(true);
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
    //   setNPCIISS(true);
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
      //const selectedRole = selectedRowKeys.data;
      console.log(selectedRows);

      setSelectedRows(selectedRows);
      const selectedList = selectedRows.map((item, index) => item.colName)
      console.log(selectedList)

      //setData(selectedList);
      //setSelectedCOl(selectedList);
      setSelectionType(selectedList);

      for (var i = 0; i < selectedList.length; i++) {
        console.log(selectedList[i]);
        setSearchText(selectedList[i]);
      }
      console.log('0th ===', selectedList[0]);

    //   if (switchCheck == 'SWITCH' && glCheck == 'GL') {
    //     checkWithGL(selectedList);
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

  //   const rowSelectiongl = {

  //     onChange:(selectedRowKeys, selectedRows) => {
  //     console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
  //       //const selectedRole = selectedRowKeys.data;
  //         console.log(selectedRows);
  //         setSelectedRows(selectedRows);
  //         const selectedList = selectedRows.map((item, index) => item.colName)
  //         console.log(selectedList)
  //         //setData(selectedList);
  //      //setSelectedCOl(selectedList);
  //       setSelectionType(selectedList);
  //       checkWithGL(selectedList);
  //     },
  //   };


//   const checkWithGL = (selectedList) => {
//     alert("checkWithGL");
//     //console.log(tblColGl);
//     // var serchString=selectedList.map((item,index)=>item);
//     var glnamess = tblColGl.map((item, index) => item.colName);
//     var swnamess = tblColSw.map((item, index) => item.colName);
//     // console.log("sw:==",swnamess);
//     //console.log(selectedList[0]);
//     //console.log(glnamess);
//     if (glnamess.includes(selectedList[0])) {
//       alert("yes");
//       //console.log("matched data",selectedList);
//       // const dataAllGLSW = selectedList.map((item, index) => ({
//       //   mappingcolumn:'sw',
//       //   colName:item,
//       //   idx:'0',
//       //   len:'0',
//       //   key:index
//       // }));
      
//       const dataAllNext = selectedList.map((item, index) => ({
//         colName:item,
//         key:index
//       }));
//       setMatchtblDataNext(dataAllNext)
//       const dataAll = selectedList.map((item, index) => ({
//         mappingcolumn:'sw',
//         colName:item,
//         idx:'0',
//         len:'0',
//         key:index
//       }));
//       const dataAll1 = selectedList.map((item, index) => ({
//         mappingcolumn: 'gl',
//         colName: item,
//         idx: '0',
//         len: '0',
//         key: index+101
//       }));

//       console.log(dataAll);
//       console.log(dataAll1);
//       //  setSWColtblData(dataAll);
//       var finalObj = dataAll.concat(dataAll1);
//       console.log(finalObj);
//        setMatchtblData(finalObj);
//        //setMatchtblData1(dataAll);
//       setMatchtbl(true);

//       // setMatchtblNew(true);
//     }
//     else {
//       alert("nooo");
//     }

//   }


//   const checkMatchtableWithNPCI = (selectedList) => {
//     alert("checkMatchtableWithNPCI");
//     //alert(selectedList[0]);
//     //console.log(tblColGl);
//     var serchString = selectedList.map((item, index) => item);
//     // var glnamess=tblColGl.map((item,index)=>item.colName);
//     // var swnamess=tblColSw.map((item,index)=>item.colName);
//     var npciissnamess = tblColNPCIISS.map((item, index) => item.colName);
//     // console.log(selectedList);


//     console.log(npciissnamess);
//     // console.log(matchtbl);
//     // console.log("sw:==",swnamess);
//     console.log(selectedList[0]);
//     // console.log(glnamess);
//     if (npciissnamess.includes(selectedList[0])) {
//       alert("yes");
//       console.log("matched data", selectedList);
//       // const dataAll = selectedList.map((item, index) => ({
//       //   colName: item,
//       //   key: index
//       // }));

//       const dataAllNext = selectedList.map((item, index) => ({
//         colName:item,
//         key:index
//       }));
//       setMatchtblDataNewISSNext(dataAllNext)
//       const dataAll = selectedList.map((item, index) => ({
//         mappingcolumn:'nw',
//         colName:item,
//         idx:'0',
//         len:'0',
//         key:index
//       }));
//       //  setSWColtblData(dataAll);
//       setMatchtblDataISSNew(dataAll);
//       //setMatchtbl(true);
//       setMatchWithNpciss(true);

//       // setMatchtblNew(true);
//     }
//     else {
//       alert("nooo");


//     }
//   }

//   const checkMatchtableWithNPCIACQ = (selectedList) => {
//     alert("checkMatchtableWithNPCIACQ");
//     //alert(selectedList[0]);
//     //console.log(tblColGl);
//     var serchString = selectedList.map((item, index) => item);
//     // var glnamess=tblColGl.map((item,index)=>item.colName);
//     // var swnamess=tblColSw.map((item,index)=>item.colName);
//     var npciacqnamess = tblColNPCIACQ.map((item, index) => item.colName);
//     // console.log(selectedList);
//     // console.log(npciissnamess);
//     // console.log(matchtbl);
//     // console.log("sw:==",swnamess);
//     console.log(selectedList[0]);
//     // console.log(glnamess);
//     if (npciacqnamess.includes(selectedList[0])) {
//       alert("yes");
//       console.log("matched data", selectedList);
//       // const dataAll = selectedList.map((item, index) => ({
//       //   colName: item,
//       //   key: index
//       // }));
//       // //  setSWColtblData(dataAll);
//       // setMatchtblDataNewACQ(dataAll);
//       // console.log(dataAll);
//       const dataAllNext = selectedList.map((item, index) => ({
//         colName:item,
//         key:index
//       }));
//       setMatchtblDataNewACQNext(dataAllNext)
//       const dataAll = selectedList.map((item, index) => ({
//         mappingcolumn:'nw',
//         colName:item,
//         idx:'0',
//         len:'0',
//         key:index
//       }));
//       // const dataAll1 = selectedList.map((item, index) => ({
//       //   mappingcolumn: 'gl',
//       //   colName: item,
//       //   idx: '0',
//       //   len: '0',
//       //   key: index+1
//       // }));

//       console.log(dataAll);
//       // console.log(dataAll1);
//       //  setSWColtblData(dataAll);
//       // var finalObj = dataAll.concat(dataAll1);
//       // console.log(finalObj);
//       setMatchtblDataNewACQ(dataAll);
     


//       //setMatchtbl(true);
//       setMatchWithNpciacq(true);


//       // setMatchtblNew(true);
//     }
//     else {
//       alert("nooo");
//     }
//   }

//   const checkMatchtableWithEJ = (selectedList) => {
//     alert("checkMatchtableWithEJ");
//     //alert(selectedList[0]);
//     //console.log(tblColGl);
//     var serchString = selectedList.map((item, index) => item);
//     // var glnamess=tblColGl.map((item,index)=>item.colName);
//     // var swnamess=tblColSw.map((item,index)=>item.colName);
//     var ejnamess = tblColEJ.map((item, index) => item.colName);
//     // console.log(selectedList);
//     console.log(ejnamess);
//     // console.log(matchtbl);
//     // console.log("sw:==",swnamess);
//     console.log(selectedList[0]);
//     // console.log(glnamess);
//     if (ejnamess.includes(selectedList[0])) {
//       alert("yes");
//       console.log("matched data", selectedList);
//       const dataAll = selectedList.map((item, index) => ({
//         mappingcolumn:'ej',
//         colName:item,
//         idx:'0',
//         len:'0',
//         key:index
//       }));
//       //  setSWColtblData(dataAll);
//       setMatchtblDataNewEJ(dataAll);
//       //setMatchtbl(true);
//       setMatchWithEJ(true);

//       // setMatchtblNew(true);
//     }
//     else {
//       alert("nooo");


//     }
//   }

  //console.log(selectionType);
  // debugger;
  const columns = [
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

  const columnsEJ = [
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
            <Card title="Join Rule Configuration" bordered={false} style={{ width: 1800 }} >

              <Form initialValues={{ remember: true }} layout={"vertical"} form={form} size={"large"}>
              <Row >
                <Col span={12} >
                <Row gutter={[8, 8]}>
                  <Col span={6}>
                    <Form.Item label="Client Name" name="clientId" >
                      <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeClientName}>
                        {ClientData}
                      </Select>
                    </Form.Item>

                  </Col>
                  <Col span={6}>
                    <Form.Item label="Channel Type" name="ChannelType" >
                      <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeChanneltName}>
                        {channeldata}
                      </Select>
                    </Form.Item>
                  </Col>
                  <Col span={6}>
                    <Form.Item label="Mode Type" name="ModeType">
                      <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeTxnMode}>
                        {modeData}
                      </Select>
                    </Form.Item>
                  </Col>
                  <Col span={6} >
                    <Form.Item label="Recon Type" name="ReconType" >
                      <Select style={{ width: 200 }} onChange={onChangeReconType}>
                        <Option value="2">2-way</Option>
                        <Option value="3">3-way</Option>
                        <Option value="4">4-way</Option>
                      </Select>
                    </Form.Item>
                  </Col>
                </Row>
                {/*<Col span={12} >
       <Form.Item label="Txn Mode" name="Txnmode" >         
           <Select
                        mode="multiple"
                        style={{ width: 300 }}
                        placeholder="select channels"
                        onChange={onChangeTxnMode}
                        optionLabelProp="label"
                      >
                        {modeData}
                </Select>                 
         </Form.Item>
       </Col>       
  </Row>*/}
                <Row>
                  {/* <Checkbox.Group  size={"large"} onChange={onChangeColumnName}> 
             {fileist}
    </Checkbox.Group>      */}
                  {fileList}

                </Row>

                <br></br>
                <Row>
                  <Form.Item>
                    <Button  >Submit</Button>
                    <Button style={{ margin: '0 8px' }} onClick={props.history.goBack} >Back</Button>
                  </Form.Item>
                </Row>
                </Col>
                <Col span={6} >

</Col></Row>
<Row>
<Col span={6}>
                      
                      {swtblloader?(
                          <Table dataSource={tblColSw} columns={columnssw} rowSelection={{
                            type: selectionType,
                            ...rowSelection,
                          }}
                            pagination={false}
                            bordered
                            scroll={{ y: 400 }}
    
                          />
                          ):(" ")}
                    </Col>
             
                  <Col span={6} >
                  {gltblloader?(
                          <Table dataSource={tblColGl} columns={columns} rowSelection={{
                            type: selectionType,
                            ...rowSelection,
                          }}
                            pagination={false}
                            bordered
                            scroll={{ y: 400 }}
    
                          />
                          ):(" ")}
                  
                  </Col>
                </Row>
             
              </Form>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default TempTableConfiguration;