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
import Highlighter from "react-highlight-words";
import { SplitButton } from 'react-bootstrap';
import SketchExample from './SketchExample';
const { Header, Content } = Layout;
const { Option } = Select;
const TempTableConfiguration = props => {
  console.log(props)
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
  const [matchtbl, setMatchtblData] = useState([])
  const [matchtblNext, setMatchtblDataNext] = useState([])
  const [matchtblnpcidata, setMatchtblDataISSNew] = useState([])
  const [matchtblnpciacqdata, setMatchtblDataNewACQ] = useState([])
  const [matchtbldataNPCIACQnext,setMatchtblDataNewACQNext]=useState([])
  const [matchtbldataNPCIISSnext,setMatchtblDataNewISSNext]=useState([])
  const [matchtbleej, setMatchtblDataNewEJ] = useState([])

  const [switchTempName, setNameSwitchTempTable] = useState('')
  const [NPCIISSTempName, setNameNPCIISSTempTable] = useState('')
  const [NPCIACQTempName, setNameNPCIACQTempTable] = useState('')
  const [npciacqtemptablename, setNewNPCIACQ] = useState('')
  const [ejTempName, setNameEJTempTable] = useState('')
  const [glTempName, setNameGLTempTable] = useState('')

  const [matchtblnpciiss, setMatchWithNpciss] = useState(false)
  const [matchtblnpciacq, setMatchWithNpciacq] = useState(false)
  const [matchtblej, setMatchWithEJ] = useState(false)
  const [selectionType, setSelectionType] = useState('');
  const [searchText, setSearchText] = useState('');
  const [filecheck, setCheckFilename] = useState([]);
  const [switchCheck, setCheckFilenameSwitch] = useState([]);
  const [npciacqcheck, setCheckFilenameNPCIACQ] = useState([]);
  const [glCheck, setCheckFilenameGL] = useState([])
  const [npciiisCheck, setCheckFilenameNPCIISS] = useState([])
  const [ejCheck, setCheckFilenameEJ] = useState([])
  const [columnnamematch, setMatchedTablesName] = useState('')
  const [joincondition, setJoinCond] = useState('')
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

  // console.log("switch tbl data",tblColSw);
  // console.log("gl tbl data",tblColGl);

  
  console.log("====================",matchtbldataNPCIACQnext);

  //For match table with gl and switch

  const EditableContext = React.createContext();

  const EditableRow = ({ index, ...props }) => {
    const [form] = Form.useForm();
    return (
      <Form form={form} component={false}>
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      </Form>
    );
  };

  const EditableCell = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    //handleSave,
    ...restProps
  }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef();
    const form = useContext(EditableContext);
    useEffect(() => {
      if (editing) {
        inputRef.current.focus();
      }
    }, [editing]);

    const toggleEdit = () => {
      setEditing(!editing);
      form.setFieldsValue({
        [dataIndex]: record[dataIndex],
      });
    };

    const handleSave = (row) => {
      // const newData = [...this.state.dataSource];
      // const index = newData.findIndex((item) => row.key === item.key);
      // const item = newData[index];
      // newData.splice(index, 1, { ...item, ...row });


      //const row = await form.validateFields();
      const newData = [...matchtbl];


      const index = newData.findIndex((item) => row.key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setMatchtblData(newData);
        // console.log(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setMatchtblData(newData);
        setEditingKey('');
      }
    };

    const save = async (e) => {
      try {
        const values = await form.validateFields();
        toggleEdit();
        handleSave({ ...record, ...values })
        //debugger;
      } catch (errInfo) {
        console.log('Save failed:', errInfo);
      }
    };


    let childNode = children;

    if (editable) {
      childNode = editing ? (
        <Form.Item
          style={{
            margin: 0,
          }}
          name={dataIndex}
          rules={[
            {
              required: true,
              message: `${title} is required.`,
            },
          ]}
        >
          <Input ref={inputRef} onPressEnter={save} onBlur={save} />
        </Form.Item>
      ) : (
          <div
            className="editable-cell-value-wrap"
            style={{
              paddingRight: 24,
            }}
            onClick={toggleEdit}
          >
            {children}
          </div>
        );
    }
    return <td {...restProps}>{childNode}</td>;
  };

  //FOR NPCI

  const EditableRowNPCI = ({ index, ...props }) => {
    const [form] = Form.useForm();
    return (
      <Form form={form} component={false}>
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      </Form>
    );
  };

  const EditableCellNPCI = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    //handleSave,
    ...restProps
  }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef();
    const form = useContext(EditableContext);
    useEffect(() => {
      if (editing) {
        inputRef.current.focus();
      }
    }, [editing]);

    const toggleEdit = () => {
      setEditing(!editing);
      form.setFieldsValue({
        [dataIndex]: record[dataIndex],
      });
    };

    const handleSave = (row) => {
      // const newData = [...this.state.dataSource];
      // const index = newData.findIndex((item) => row.key === item.key);
      // const item = newData[index];
      // newData.splice(index, 1, { ...item, ...row });


      //const row = await form.validateFields();
      const newData = [...matchtblnpciacqdata];


      const index = newData.findIndex((item) => row.key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setMatchtblDataNewACQ(newData);
        // console.log(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setMatchtblDataNewACQ(newData);
        setEditingKey('');
      }
    };

    const save = async (e) => {
      try {
        const values = await form.validateFields();
        toggleEdit();
        handleSave({ ...record, ...values })
        //debugger;
      } catch (errInfo) {
        console.log('Save failed:', errInfo);
      }
    };


    let childNode = children;

    if (editable) {
      childNode = editing ? (
        <Form.Item
          style={{
            margin: 0,
          }}
          name={dataIndex}
          rules={[
            {
              required: true,
              message: `${title} is required.`,
            },
          ]}
        >
          <Input ref={inputRef} onPressEnter={save} onBlur={save} />
        </Form.Item>
      ) : (
          <div
            className="editable-cell-value-wrap"
            style={{
              paddingRight: 24,
            }}
            onClick={toggleEdit}
          >
            {children}
          </div>
        );
    }
    return <td {...restProps}>{childNode}</td>;
  };

  //for EJ

  //FOR NPCI

  const EditableRowEJ = ({ index, ...props }) => {
    const [form] = Form.useForm();
    return (
      <Form form={form} component={false}>
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      </Form>
    );
  };

  const EditableCellEJ = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    //handleSave,
    ...restProps
  }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef();
    const form = useContext(EditableContext);
    useEffect(() => {
      if (editing) {
        inputRef.current.focus();
      }
    }, [editing]);

    const toggleEdit = () => {
      setEditing(!editing);
      form.setFieldsValue({
        [dataIndex]: record[dataIndex],
      });
    };

    const handleSave = (row) => {
      // const newData = [...this.state.dataSource];
      // const index = newData.findIndex((item) => row.key === item.key);
      // const item = newData[index];
      // newData.splice(index, 1, { ...item, ...row });


      //const row = await form.validateFields();
      const newData = [...matchtbleej];


      const index = newData.findIndex((item) => row.key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setMatchtblDataNewEJ(newData);
        // console.log(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setMatchtblDataNewEJ(newData);
        setEditingKey('');
      }
    };

    const save = async (e) => {
      try {
        const values = await form.validateFields();
        toggleEdit();
        handleSave({ ...record, ...values })
        //debugger;
      } catch (errInfo) {
        console.log('Save failed:', errInfo);
      }
    };


    let childNode = children;

    if (editable) {
      childNode = editing ? (
        <Form.Item
          style={{
            margin: 0,
          }}
          name={dataIndex}
          rules={[
            {
              required: true,
              message: `${title} is required.`,
            },
          ]}
        >
          <Input ref={inputRef} onPressEnter={save} onBlur={save} />
        </Form.Item>
      ) : (
          <div
            className="editable-cell-value-wrap"
            style={{
              paddingRight: 24,
            }}
            onClick={toggleEdit}
          >
            {children}
          </div>
        );
    }
    return <td {...restProps}>{childNode}</td>;
  };

  //for NPCI ISS
  const EditableRowNPCIISS = ({ index, ...props }) => {
    const [form] = Form.useForm();
    return (
      <Form form={form} component={false}>
        <EditableContext.Provider value={form}>
          <tr {...props} />
        </EditableContext.Provider>
      </Form>
    );
  };

  const EditableCellNPCIISS = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    //handleSave,
    ...restProps
  }) => {
    const [editing, setEditing] = useState(false);
    const inputRef = useRef();
    const form = useContext(EditableContext);
    useEffect(() => {
      if (editing) {
        inputRef.current.focus();
      }
    }, [editing]);

    const toggleEdit = () => {
      setEditing(!editing);
      form.setFieldsValue({
        [dataIndex]: record[dataIndex],
      });
    };

    const handleSave = (row) => {
      // const newData = [...this.state.dataSource];
      // const index = newData.findIndex((item) => row.key === item.key);
      // const item = newData[index];
      // newData.splice(index, 1, { ...item, ...row });


      //const row = await form.validateFields();
      const newData = [...matchtblnpcidata];


      const index = newData.findIndex((item) => row.key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setMatchtblDataISSNew(newData);
        // console.log(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setMatchtblDataISSNew(newData);
        setEditingKey('');
      }
    };

    const save = async (e) => {
      try {
        const values = await form.validateFields();
        toggleEdit();
        handleSave({ ...record, ...values })
        //debugger;
      } catch (errInfo) {
        console.log('Save failed:', errInfo);
      }
    };


    let childNode = children;

    if (editable) {
      childNode = editing ? (
        <Form.Item
          style={{
            margin: 0,
          }}
          name={dataIndex}
          rules={[
            {
              required: true,
              message: `${title} is required.`,
            },
          ]}
        >
          <Input ref={inputRef} onPressEnter={save} onBlur={save} />
        </Form.Item>
      ) : (
          <div
            className="editable-cell-value-wrap"
            style={{
              paddingRight: 24,
            }}
            onClick={toggleEdit}
          >
            {children}
          </div>
        );
    }
    return <td {...restProps}>{childNode}</td>;
  };



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
      alert("inside mode")
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
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
//@GetMapping("getFileDataCol1/{fileName}")
//public List[] getFileDataCol1(@PathVariable("fileName") String fileName)
      const fileResponse = await axios.get(`getFileDataCol1/${value}`);
      console.log(fileResponse.data)
      const colResult = fileResponse.data;
      const columnNamess = colResult[0];
      //console.log(columnNamess);
      const tempFileName = colResult[1]
      //console.log(tempFileName)
      setLoader(false);
      if (value == "SWITCH") {
        const dataAll = columnNamess.map((item, index) => ({
          colName: item.columnName,
          chosen: true,
          key: index
        }));
        setSWColtblData(dataAll);
        const tblNameSwitch = tempFileName.map((item, index) => item.tableName);
        //console.log(tblNameSwitch)
        setNameSwitchTempTable(tblNameSwitch[0]);
        //    alert(tblNameSwitch[0])
      }
      if (value == "GL") {
        // console.log(tblcoltest);
        const columnNamess = colResult[0];
      //  console.log(columnNamess);
        const tempFileName = colResult[1]
        //console.log(tempFileName)
        const dataAll = columnNamess.map((item, index) => ({
          colName: item.columnName,
          chosen: false,
          key: index
        }));
        setGLColtblData(dataAll);
        const tblNameGL = tempFileName.map((item, index) => item.tableName);
        setNameGLTempTable(tblNameGL[0]);

        // console.log(tblcoltest);
      }

      if (value == "NPCIISS") {
        //console.log(tblcoltest);
        const columnNamess = colResult[0];
        //console.log(columnNamess);
        const tempFileName = colResult[1]
        //console.log(tempFileName)
        const dataAll = columnNamess.map((item, index) => ({
          colName: item.columnName,
          chosen: false,
          key: index
        }));
        setNPCIISSColtblData(dataAll);
        const tblNameNPCIISS = tempFileName.map((item, index) => item.tableName);
        setNameNPCIISSTempTable(tblNameNPCIISS[0]);
        console.log(tblcoltest);
      }

      if (value == "NPCIACQ") {
        const columnNamess = colResult[0];
        console.log(columnNamess);
        const tempFileName = colResult[1];
        console.log(tempFileName);
        const dataAll = columnNamess.map((item, index) => ({
          colName: item.columnName,
          chosen: false,
          key: index
        }));
        setNPCIACQColtblData(dataAll);
        const tblNameNPCIACQ = tempFileName.map((item, index) => item.tableName);
        console.log(tblNameNPCIACQ[0]);
        setNameNPCIACQTempTable(tblNameNPCIACQ[0]);
        setNewNPCIACQ(tblNameNPCIACQ[0]);
        console.log(tblcoltest);
      }

      if (value == "EJ") {
        alert("in ej ");
        console.log(NPCIACQTempName);
        const columnNamess = colResult[0];
        console.log(columnNamess);
        const tempFileNameej = colResult[1];
        console.log(tempFileNameej);
        const dataAll = columnNamess.map((item, index) => ({
          colName: item.columnName,
          chosen: false,
          key: index
        }));
        setEJColtblData(dataAll);
        const tblNameEJ = tempFileNameej.map((item, index) => item.tableName);
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

  const fiterSelectedRows = tblColSw.filter(row => {
    return row.chosen;
  });


  
  const menuData = props.location.state;
  console.log(menuData);

  function onChangeColumnNameSwitch(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck = `${e.target.value}`;
    setCheckFilenameSwitch(filelistCheck);
    alert(filelistCheck);
    if (filelistCheck == 'SWITCH') {
      getFileDataCol(filelistCheck);
      setSWtbl(true);
      setMatchtblNew(false);
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
      // setMatchtblNew(false);
      setCheckFilenameSwitch('');
      setCheckFilenameGL('');
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
      // setMatchtblNew(false);
      setCheckFilenameSwitch('');
      setCheckFilenameGL('');
      setCheckFilenameNPCIACQ('');
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
      setMatchtblNew(false);
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
      setCheckFilenameGL('');
      setCheckFilenameSwitch('');

    }
  }
  function onChangeColumnName(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck = `${e.target.value}`;
    setCheckFilename(filelistCheck);
    console.log(filelistCheck);
    // setCOlumnName(checkedValues);
    var arrayofcheckfilename = [];
    arrayofcheckfilename.push(filelistCheck);
    console.log(arrayofcheckfilename);

    if (filelistCheck == 'GL') {
      getFileDataCol(filelistCheck);
      setGLtbl(true);
    }
    if (filelistCheck == 'SWITCH') {
      getFileDataCol(filelistCheck);
      setSWtbl(true);
    }
    if (filelistCheck == 'NPCIISS') {
      getFileDataCol(filelistCheck);
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

      if (switchCheck == 'SWITCH' && glCheck == 'GL') {
        checkWithGL(selectedList);
      }
      console.log(columnnamematch);
      if (/*columnnamematch=='GLCBSTEMP = SWITCHTEMP'*/ npciiisCheck == 'NPCIISS') {
        checkMatchtableWithNPCI(selectedList);
      }

      if (npciacqcheck == 'NPCIACQ') {
        checkMatchtableWithNPCIACQ(selectedList);
      }

      if (ejCheck == 'EJ') {
        checkMatchtableWithEJ(selectedList);
      }

    },

    getCheckboxProps(record) {
     // console.log(record.colName);
      return {
        props: {
          name: !record.colName ? 'disabled' : '',
          //disabled: record.isDisabled || !record.colName
        }
      }
    }

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


  const checkWithGL = (selectedList) => {
    alert("checkWithGL");
    //console.log(tblColGl);
    // var serchString=selectedList.map((item,index)=>item);
    var glnamess = tblColGl.map((item, index) => item.colName);
    var swnamess = tblColSw.map((item, index) => item.colName);
    // console.log("sw:==",swnamess);
    //console.log(selectedList[0]);
    //console.log(glnamess);
    if (glnamess.includes(selectedList[0])) {
      alert("yes");
      //console.log("matched data",selectedList);
      // const dataAllGLSW = selectedList.map((item, index) => ({
      //   mappingcolumn:'sw',
      //   colName:item,
      //   idx:'0',
      //   len:'0',
      //   key:index
      // }));
      
      const dataAllNext = selectedList.map((item, index) => ({
        colName:item,
        key:index
      }));
      setMatchtblDataNext(dataAllNext)
      const dataAll = selectedList.map((item, index) => ({
        mappingcolumn:'sw',
        colName:item,
        idx:'0',
        len:'0',
        key:index
      }));
      const dataAll1 = selectedList.map((item, index) => ({
        mappingcolumn: 'gl',
        colName: item,
        idx: '0',
        len: '0',
        key: index+101
      }));

      console.log(dataAll);
      console.log(dataAll1);
      //  setSWColtblData(dataAll);
      var finalObj = dataAll.concat(dataAll1);
      console.log(finalObj);
       setMatchtblData(finalObj);
       //setMatchtblData1(dataAll);
      setMatchtbl(true);

      // setMatchtblNew(true);
    }
    else {
      alert("nooo");
    }

  }


  const checkMatchtableWithNPCI = (selectedList) => {
    alert("checkMatchtableWithNPCI");
    //alert(selectedList[0]);
    //console.log(tblColGl);
    var serchString = selectedList.map((item, index) => item);
    // var glnamess=tblColGl.map((item,index)=>item.colName);
    // var swnamess=tblColSw.map((item,index)=>item.colName);
    var npciissnamess = tblColNPCIISS.map((item, index) => item.colName);
    // console.log(selectedList);


    console.log(npciissnamess);
    // console.log(matchtbl);
    // console.log("sw:==",swnamess);
    console.log(selectedList[0]);
    // console.log(glnamess);
    if (npciissnamess.includes(selectedList[0])) {
      alert("yes");
      console.log("matched data", selectedList);
      // const dataAll = selectedList.map((item, index) => ({
      //   colName: item,
      //   key: index
      // }));

      const dataAllNext = selectedList.map((item, index) => ({
        colName:item,
        key:index
      }));
      setMatchtblDataNewISSNext(dataAllNext)
      const dataAll = selectedList.map((item, index) => ({
        mappingcolumn:'nw',
        colName:item,
        idx:'0',
        len:'0',
        key:index
      }));
      //  setSWColtblData(dataAll);
      setMatchtblDataISSNew(dataAll);
      //setMatchtbl(true);
      setMatchWithNpciss(true);

      // setMatchtblNew(true);
    }
    else {
      alert("nooo");


    }
  }

  const checkMatchtableWithNPCIACQ = (selectedList) => {
    alert("checkMatchtableWithNPCIACQ");
    //alert(selectedList[0]);
    //console.log(tblColGl);
    var serchString = selectedList.map((item, index) => item);
    // var glnamess=tblColGl.map((item,index)=>item.colName);
    // var swnamess=tblColSw.map((item,index)=>item.colName);
    var npciacqnamess = tblColNPCIACQ.map((item, index) => item.colName);
    // console.log(selectedList);
    // console.log(npciissnamess);
    // console.log(matchtbl);
    // console.log("sw:==",swnamess);
    console.log(selectedList[0]);
    // console.log(glnamess);
    if (npciacqnamess.includes(selectedList[0])) {
      alert("yes");
      console.log("matched data", selectedList);
      // const dataAll = selectedList.map((item, index) => ({
      //   colName: item,
      //   key: index
      // }));
      // //  setSWColtblData(dataAll);
      // setMatchtblDataNewACQ(dataAll);
      // console.log(dataAll);
      const dataAllNext = selectedList.map((item, index) => ({
        colName:item,
        key:index
      }));
      setMatchtblDataNewACQNext(dataAllNext)
      const dataAll = selectedList.map((item, index) => ({
        mappingcolumn:'nw',
        colName:item,
        idx:'0',
        len:'0',
        key:index
      }));
      // const dataAll1 = selectedList.map((item, index) => ({
      //   mappingcolumn: 'gl',
      //   colName: item,
      //   idx: '0',
      //   len: '0',
      //   key: index+1
      // }));

      console.log(dataAll);
      // console.log(dataAll1);
      //  setSWColtblData(dataAll);
      // var finalObj = dataAll.concat(dataAll1);
      // console.log(finalObj);
      setMatchtblDataNewACQ(dataAll);
     


      //setMatchtbl(true);
      setMatchWithNpciacq(true);


      // setMatchtblNew(true);
    }
    else {
      alert("nooo");
    }
  }

  const checkMatchtableWithEJ = (selectedList) => {
    alert("checkMatchtableWithEJ");
    //alert(selectedList[0]);
    //console.log(tblColGl);
    var serchString = selectedList.map((item, index) => item);
    // var glnamess=tblColGl.map((item,index)=>item.colName);
    // var swnamess=tblColSw.map((item,index)=>item.colName);
    var ejnamess = tblColEJ.map((item, index) => item.colName);
    // console.log(selectedList);
    console.log(ejnamess);
    // console.log(matchtbl);
    // console.log("sw:==",swnamess);
    console.log(selectedList[0]);
    // console.log(glnamess);
    if (ejnamess.includes(selectedList[0])) {
      alert("yes");
      console.log("matched data", selectedList);
      const dataAll = selectedList.map((item, index) => ({
        mappingcolumn:'ej',
        colName:item,
        idx:'0',
        len:'0',
        key:index
      }));
      //  setSWColtblData(dataAll);
      setMatchtblDataNewEJ(dataAll);
      //setMatchtbl(true);
      setMatchWithEJ(true);

      // setMatchtblNew(true);
    }
    else {
      alert("nooo");


    }
  }

  console.log(selectionType);
  // debugger;
  const columns = [
    {
      title: 'GL Column Name',
      dataIndex: 'colName',
      key: 'index',
      render: text =>
        text ? (
          <Highlighter
            highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
            searchWords={[searchText]}
            autoescap="true"
            textToHighlight={text.toString()}
          />

        ) : null,

    }
  ];

  const columnssw = [
    {
      title: 'SWITCH Column Name',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
      render: text =>
        text ? (
          <Highlighter
            highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
            searchWords={[searchText]}
            autoescap="true"
            textToHighlight={text.toString()}
          />

        ) : null,

    }
  ];


  const columnsnpciiss = [
    {
      title: 'NPCI Column Name',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
      render: text =>
        text ? (
          <Highlighter
            highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
            searchWords={[searchText]}
            autoescap="true"
            textToHighlight={text.toString()}
          />

        ) : null,

    }
  ];


  const columnsnpciacq = [
    {
      title: 'NPCI Column Name',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',

    }
  ];

  const columnsEJ = [
    {
      title: 'EJ Column Name',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
      render: text =>
        text ? (
          <Highlighter
            highlightStyle={{ backgroundColor: "#ffc069", padding: 0 }}
            searchWords={[searchText]}
            autoescap="true"
            textToHighlight={text.toString()}
          />

        ) : null,

    }
  ];

  const columnsmatchNext=[
    {
      title: 'match Column Name gl with switch',
      dataIndex: 'colName',
      key: 'index',
      //editable: true
      //   width: '5%',
    }
  ];

  // const columnsmatchNext=[
  //   {
  //     title: 'match Column Name gl with switch',
  //     dataIndex: 'colName',
  //     key: 'index',
  //     //editable: true
  //     //   width: '5%',
  //   }
  // ];
  //matchtbldataNPCIACQnext

  const columnsmatch = [
     {
    title: 'match Column Name gl with switch',
      dataIndex: 'colName',
      key: 'index',
      //editable: true
      //   width: '5%',
    },
    {
      title: 'columnindex',
      dataIndex: 'idx',
      key: 'index',
      editable: true
    //   width: '5%',
    },
    {
      title:'columnlen',
      dataIndex: 'len',
      key: 'index',
      editable: true
    },
    {
      title: 'mappingcolumn',
      dataIndex: 'mappingcolumn',
      key: 'index',
     //editable: true
    //   width: '5%',
    }
    
  ];

  
  const columnsmatchNpciacq = [
    {
      title: 'match Column Name gl,switch and NPCI',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
    },
    
  ];



  const columnsmatchwithNPCIISS = [
    {
      title: 'match Column Name with npciiss',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
    },
    {
      title: 'columnindex',
      dataIndex: 'idx',
      key: 'index',
      editable: true
    //   width: '5%',
    },
    {
      title:'columnlen',
      dataIndex: 'len',
      key: 'index',
      editable: true
    },
    {
      title: 'mappingcolumn',
      dataIndex: 'mappingcolumn',
      key: 'index',
     //editable: true
    //   width: '5%',
    }
    


  ];

  const columnsmatchwithNPCIACQ = [
    {
      title: 'match Column Name with npciacq',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
    },
    {
      title: 'columnindex',
      dataIndex: 'idx',
      key: 'index',
      editable: true
    //   width: '5%',
    },
    {
      title:'columnlen',
      dataIndex: 'len',
      key: 'index',
      editable: true
    },
    {
      title: 'mappingcolumn',
      dataIndex: 'mappingcolumn',
      key: 'index',
     //editable: true
    //   width: '5%',
    }
  ];

  const columnsmatchwithEJ = [
    {
      title: 'match Column Name with EJ',
      dataIndex: 'colName',
      key: 'index',
      //   width: '5%',
    },
    {
      title: 'columnindex',
      dataIndex: 'idx',
      key: 'index',
      editable: true
    //   width: '5%',
    },
    {
      title:'columnlen',
      dataIndex: 'len',
      key: 'index',
      editable: true
    },
    {
      title: 'mappingcolumn',
      dataIndex: 'mappingcolumn',
      key: 'index',
     //editable: true
    //   width: '5%',
    }
  ];
  // const columnIndexLenght = [
  //   {
  //     title: 'name',
  //     dataIndex: 'name',
  //     key: 'index',
  //     //   width: '5%',
  //   },
  //   {
  //     title: 'Start Index',
  //     dataIndex: 'idx',
  //     key: 'index',
  //     editable: true
  //     //   width: '5%',
  //   },
  //   {
  //     title: 'Lenght',
  //     dataIndex: 'len',
  //     key: 'index',
  //     editable: true
  //     //   width: '5%',
  //   }
  // ];


  const mergedColumnsnpciACQ = columnsmatchwithNPCIACQ.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        //      handleSave: this.handleSave,
      }),
    };
  });

  const mergedColumnsGlSW = columnsmatch.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        //      handleSave: this.handleSave,
      }),
    };
  });

  //columnsmatchwithEJ

  const mergedColumnsEJ = columnsmatchwithEJ.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        //      handleSave: this.handleSave,
      }),
    };
  });


  
  //columnsmatchwithNPCIISS

  const mergedColumnsNPCIISS = columnsmatchwithNPCIISS.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        //      handleSave: this.handleSave,
      }),
    };
  });

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
             
              </Form>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default TempTableConfiguration;