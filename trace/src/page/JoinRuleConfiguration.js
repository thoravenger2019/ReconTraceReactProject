import React, { useState,useEffect } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
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
} from 'antd';
import Title from 'antd/lib/typography/Title';
import Highlighter from "react-highlight-words";
import { SplitButton } from 'react-bootstrap';
import SketchExample from './SketchExample';
const {Header, Content}=Layout;
const { Option } = Select;

const JoinRuleConfiguration = props => {
  console.log(props)  
  const [clientid, setClientID] = useState([])
  const [ruletype,setRultType] = useState([])
  const [channeldata,setChannelData] = useState([])
  const [ClientData, setClientData] = useState([])
  const [modeData,setModeData]=useState([])
  const [channelid,setChannelID]=useState([])
  const [modeid,setModeID]=useState([])
  const [columnname,setCOlSWumnName]=useState([])
  const [fileList,setFileList]=useState([])
  const [tblCol,setColtblData]=useState([])
  const [tblColSw,setSWColtblData]=useState([])
  const [tblColGl,setGLColtblData]=useState([])
  const [tblColNPCIISS,setNPCIISSColtblData]=useState([])
  const [tblcoltest,setSelectedRows]=useState([])
  const [matchtbl,setMatchtblData]=useState([])
  const [matchtblnpcidata,setMatchtblDataNew]=useState([])
  const [switchTempName,setNameSwitchTempTable]=useState('')
  const [NPCIISSTempName,setNameNPCIISSTempTable]=useState('')
  const [glTempName,setNameGLTempTable]=useState('')

  const [matchtblnpciiss,setMatchWithNpciss]=useState(false)

  const [selectionType, setSelectionType] = useState('');
  const [searchText, setSearchText] = useState('');
  const [filecheck,setCheckFilename]=useState([]);
  const [switchCheck,setCheckFilenameSwitch]=useState([]);
  const [glCheck,setCheckFilenameGL]=useState([])
  const [npciiisCheck,setCheckFilenameNPCIISS]=useState([])
  const [columnnamematch,setMatchedTablesName]=useState('')
  const [joincondition,setJoinCond]=useState('')
//   const [branchdata, setBranchData] = useState([])
   const [loader, setLoader] = useState(true)
   const [gltblloader,setGLtbl]=useState(false)
   const [swtblloader,setSWtbl]=useState(false)
   const [npciiss,setNPCIISS]=useState(false)
   const [matchtblloader,setMatchtbl]=useState(false);
   const [matchtblNew,setMatchtblNew]=useState(false);
   const [joinCondLoader,setJoinCondLoader]=useState(false);

// console.log("switch tbl data",tblColSw);
// console.log("gl tbl data",tblColGl);

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
      let selectedclientID=value;
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

        console.log("rule id",ruletype);
        //const modeResponse = await axios.get(`getmatchingmodeinfo/${clientid}/${value}`);
        const fileResponse = await axios.get(`getfiletypes/${channelid}`);
        
        console.log(fileResponse.data);
        
       
        setLoader(false);
        var filelist=fileResponse.data;
        var result=filelist.map((item)=>item.fileList);
        console.log(result);
        var splitresult=result.toString().split(',');
        console.log(splitresult);
        // var finalListFile=splitresult.map((item,index)=>
        //     <Checkbox value={item}  key={index} onChange={onChangeColumnName}>{item}</Checkbox>
        // )
        // setFileList(finalListFile);
//    console.log("rule type",value);
        if(modeid==3 && value==3){
            alert(modeid);
            alert(ruletype);
            if(splitresult.includes("SWITCH") && splitresult.includes("GL") && splitresult.includes("NPCIISS")){
                alert("ej gl");
                var finalListFile=<div><Checkbox value={splitresult[4]}  key={1} onChange={onChangeColumnNameSwitch}>{splitresult[4]}</Checkbox><Checkbox value={splitresult[3]}  key={2} onChange={onChangeColumnNameGL}>{splitresult[3]}</Checkbox><Checkbox value={splitresult[2]}  key={3} onChange={onChangeColumnNameNPCIISS}>{splitresult[2]}</Checkbox></div>
                // +
                // <Checkbox value={splitresult[3]}  key={2} onChange={onChangeColumnName}>{splitresult[3]}</Checkbox>
                // // +
                // <Checkbox value={splitresult[2]}  key={3} onChange={onChangeColumnName}>{splitresult[2]}</Checkbox>;
                console.log(finalListFile);
            setFileList(finalListFile);
            }else{
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

const getFileDataCol= async (value) => {
    try {
        ///alert("client id"+ClientData);
        //alert("channel id"+value);

        const fileResponse = await axios.get(`getFileDataCol/${value}`);
        console.log(fileResponse.data)
        const colResult=fileResponse.data;
        const columnNamess=colResult[0];
        console.log(columnNamess);
        const tempFileName=colResult[1]
        console.log(tempFileName)
        setLoader(false);
        if(value=="SWITCH"){
            const dataAll=columnNamess.map((item,index)=>({
                colName : item.columnName,
                chosen: true,
                key: index
            }));
            setSWColtblData(dataAll);
            const tblNameSwitch=tempFileName.map((item,index)=>item.tableName);
            console.log(tblNameSwitch)
            setNameSwitchTempTable(tblNameSwitch[0]);
        //    alert(tblNameSwitch[0])
        }
        if(value=="GL"){
           // console.log(tblcoltest);
            const columnNamess=colResult[0];
            console.log(columnNamess);
            const tempFileName=colResult[1]
            console.log(tempFileName)
            const dataAll=columnNamess.map((item,index)=>({
                colName : item.columnName,
                chosen: false,
                key: index
            }));
            setGLColtblData(dataAll);
            const tblNameGL=tempFileName.map((item,index)=>item.tableName);
            setNameGLTempTable(tblNameGL[0]);

           // console.log(tblcoltest);
        }

        if(value=="NPCIISS"){
            //console.log(tblcoltest);
            const columnNamess=colResult[0];
            console.log(columnNamess);
            const tempFileName=colResult[1]
            console.log(tempFileName)
            const dataAll=columnNamess.map((item,index)=>({
                colName : item.columnName,
                chosen: false,
                key: index
            }));
            setNPCIISSColtblData(dataAll);
            const tblNameNPCIISS=tempFileName.map((item,index)=>item.tableName);
            setNameNPCIISSTempTable(tblNameNPCIISS[0]);
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


const addJoinRuleDetails = async () => {
    try {
        console.log(switchCheck);
        console.log(glCheck);
        const validateFields = await form.validateFields();
        const values = form.getFieldsValue();
        console.log(values);
      // console.log(filecheck)  
       if(switchCheck=='SWITCH' && glCheck=='GL')   {
           //alert("hii... ");
          // console.log(selectionType);
        const response = await axios.get(`joinopt/${clientid}/${channelid}/${modeid}/${ruletype}/${switchCheck+','+glCheck}/${switchTempName}/${glTempName}/${selectionType}`);
        console.log(response.data);
        var matchtableresp=response.data;
        var MatchedTablesName=matchtableresp.map((item,index)=>item.MatchedTables);
        var joinCond=matchtableresp.map((item,index)=>item.joindString);
        console.log(MatchedTablesName);
        setMatchedTablesName(MatchedTablesName[0]);
        setJoinCond(joinCond[0])
        if(matchtableresp!=""){
            alert("workin not equal");
            setGLtbl(false);
            setSWtbl(false);
            setMatchtbl(false);
            setMatchWithNpciss(false);
            setMatchtblNew(true);
            setJoinCondLoader(true);
        }
       }                                                                                                                               

    if(npciiisCheck=='NPCIISS')   {
        //alert("hii... ");
       // console.log(selectionType);
     const response = await axios.get(`joinopt/${clientid}/${channelid}/${modeid}/${ruletype}/${npciiisCheck}/${glTempName+"-"+switchTempName}/${NPCIISSTempName}/${selectionType}`);
     console.log(response.data);
     var matchtableresp=response.data;
     var MatchedTablesName=matchtableresp.map((item,index)=>item.MatchedTables);
     var joinCond=matchtableresp.map((item,index)=>item.joindString);
     console.log(MatchedTablesName);
     setMatchedTablesName(MatchedTablesName[0]);
     setJoinCond(joinCond[0])
     if(matchtableresp!=""){
         alert("workin not equal");
         setGLtbl(false);
         setSWtbl(false);
         setMatchtbl(false);
         setMatchWithNpciss(false);
         setMatchtblNew(true);
         setJoinCondLoader(true);
     }
    }                                                                                                                               

    //    const colResult=response.data;
    //    const dataAll=colResult.map((item,index)=>({
    //        colName : item.MatchingColumn,
    //        key: index
    //    }));
    //    setColtblData(dataAll);
    // //    const responseAddRule=response.data;
    //    const rdata = responseAddRule.map((item, index) => item.Status );
    //     console.log(rdata);
    //     const abc=JSON.stringify(rdata); 
    //      console.log(abc);
    //    if (JSON.stringify(rdata) === '["[record insert]"]') {
    //         alert("inserted successfully");
    //     }
        

    //    window.location.reload(false);
       

        //props.history.push("/AddUser",response.data)
    } catch (e) {
        console.log(e);
    }
}

  const menuData = props.location.state;
  console.log(menuData);

  function onChangeColumnNameSwitch(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck=`${e.target.value}`;
    setCheckFilenameSwitch(filelistCheck);
    alert(filelistCheck);
    if(filelistCheck=='SWITCH'){
        getFileDataCol(filelistCheck);
        setSWtbl(true);
        setMatchtblNew(false);
    }
}


function onChangeColumnNameGL(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck=`${e.target.value}`;
    setCheckFilenameGL(filelistCheck);
    alert(filelistCheck);
    if(filelistCheck=='GL'){
        getFileDataCol(filelistCheck);
        setGLtbl(true);
        setMatchtblNew(false);
    }
}


function onChangeColumnNameNPCIISS(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck=`${e.target.value}`;
    setCheckFilenameNPCIISS(filelistCheck);
    alert(filelistCheck);
    if(filelistCheck=='NPCIISS'){
        getFileDataCol(filelistCheck);
        setNPCIISS(true);
        setCheckFilenameGL('');
        setCheckFilenameSwitch('');
       
    }
}
  function onChangeColumnName(e) {
    console.log(`checked = ${e.target.value}`);
    const filelistCheck=`${e.target.value}`;
    setCheckFilename(filelistCheck);
    console.log(filelistCheck);
    // setCOlumnName(checkedValues);
    var arrayofcheckfilename=[];
    arrayofcheckfilename.push(filelistCheck);
    console.log(arrayofcheckfilename);

    if(filelistCheck=='GL'){
        getFileDataCol(filelistCheck);
        setGLtbl(true);
    }
    if(filelistCheck=='SWITCH'){
        getFileDataCol(filelistCheck);
        setSWtbl(true);
    }
    if(filelistCheck=='NPCIISS'){
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
    onChange:(selectedRowKeys, selectedRows) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
      //const selectedRole = selectedRowKeys.data;
        console.log(selectedRows);
        setSelectedRows(selectedRows);
        const selectedList = selectedRows.map((item, index) => item.colName)
        console.log(selectedList)

        //setData(selectedList);
     //setSelectedCOl(selectedList);
      setSelectionType(selectedList);
   
      for(var i=0; i<selectedList.length;i++){
        console.log(selectedList[i]);
        setSearchText(selectedList[i]);
      }
      console.log('0th ===',selectedList[0]);

      if(switchCheck=='SWITCH' && glCheck=='GL' )   
      {
        checkWithGL(selectedList);
      }
      console.log(columnnamematch);
      if(/*columnnamematch=='GLCBSTEMP = SWITCHTEMP'*/ npciiisCheck=='NPCIISS' )
      {
        checkMatchtableWithNPCI(selectedList);
      }

    },

  
    // getCheckboxProps: record => ({
    //   enabled : record.isActivie === 'Disabled User', // Column configuration not to be checked
    //   isActivie: record.isActivie,

    // }),
   
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


  const checkWithGL=(selectedList)=>{
    alert("checkWithGL");
    //console.log(tblColGl);
    // var serchString=selectedList.map((item,index)=>item);
    var glnamess=tblColGl.map((item,index)=>item.colName);
    var swnamess=tblColSw.map((item,index)=>item.colName);
   // console.log("sw:==",swnamess);
    //console.log(selectedList[0]);
    //console.log(glnamess);
    if(glnamess.includes(selectedList[0])){
    alert("yes");
    //console.log("matched data",selectedList);
    const dataAll=selectedList.map((item,index)=>({
        colName : item,
        key: index
    }));
  //  setSWColtblData(dataAll);
       
       setMatchtblData(dataAll);
       setMatchtbl(true);
      
      // setMatchtblNew(true);
    }
    else{
        alert("nooo");
    }

  }

  
  const checkMatchtableWithNPCI=(selectedList)=>{
      alert("checkMatchtableWithNPCI");
    //alert(selectedList[0]);
    //console.log(tblColGl);
    var serchString=selectedList.map((item,index)=>item);
    // var glnamess=tblColGl.map((item,index)=>item.colName);
    // var swnamess=tblColSw.map((item,index)=>item.colName);
    var npciissnamess=tblColNPCIISS.map((item,index)=>item.colName);
    // console.log(selectedList);
     console.log(npciissnamess);
    // console.log(matchtbl);
    // console.log("sw:==",swnamess);
     console.log(selectedList[0]);
    // console.log(glnamess);
    if(npciissnamess.includes(selectedList[0])){
    alert("yes");
    console.log("matched data",selectedList);
    const dataAll=selectedList.map((item,index)=>({
        colName : item,
        key: index
    }));
  //  setSWColtblData(dataAll);
       setMatchtblDataNew(dataAll);
       //setMatchtbl(true);
       setMatchWithNpciss(true);

      // setMatchtblNew(true);
    }
    else{
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
      ) : null
    //   width: '5%',
    }
 ];

 const columnssw = [
    {
      title: 'SWITCH Column Name',
      dataIndex: 'colName',
      key: 'index',
    //   width: '5%',

    }
 ];

 
 const columnsnpciiss = [
    {
      title: 'NPCI Column Name',
      dataIndex: 'colName',
      key: 'index',
    //   width: '5%',

    }
 ];

 const columnsmatch = [
    {
      title: 'match Column Name gl with switch',
      dataIndex: 'colName',
      key: 'index',
    //   width: '5%',
    }
 ];


 


 const columnsmatchwithNPCIISS = [
    {
      title: 'match Column Name with npciiss',
      dataIndex: 'colName',
      key: 'index',
    //   width: '5%',
    }
 ];

 const columnsmatchnew = [
    {
      title: 'new match gl switch',
      dataIndex: 'colName',
      key: 'index',
    //   width: '5%',
    }
 ];
 


  const [form] = Form.useForm()

  const getinfofromjointables= async()=> {

    try{
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
    }catch(e) {
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
         <Header style={{padding:"20px"}}>
            <Avatar shape ="square" style ={{float :"right"}} size="default" src="./max.png"/>
            <Title
             level={3} style={{color:"white"}}>Trace</Title>
            </Header>
        <Layout>
             <MenuSideBar menuData={menuData}/>
      <Layout style ={{height:"100vh",backgroundColor:"white"}}>
      <Content>
    <Card title="Join Rule Configuration" bordered={false} style={{ width: 1800 }} >
      
    <Form  initialValues={{ remember: true }} layout={"vertical"}  form={form} size={"large"}>
    <Row gutter={40}>
      <Col span={4}>
      <Form.Item label="Client Name" name="clientId" >
         <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeClientName}>
                              {ClientData}
        </Select>                  
      </Form.Item>
       
       </Col>
      <Col span={4}>
        <Form.Item label="Channel Type" name="ChannelType" >
          <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeChanneltName}>
            {channeldata}
           </Select>                  
         </Form.Item>       
      </Col>
     <Col span={4}>
     <Form.Item label="Mode Type" name="ModeType">
          <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeTxnMode}>
            {modeData}
           </Select>                  
         </Form.Item>       
       </Col>
       <Col span={4} >
       <Form.Item label="Recon Type" name="ReconType" >
          <Select  style={{ width: 200 }} onChange={onChangeReconType}>
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
                   <Button onClick={addJoinRuleDetails} >Submit</Button>             
                   <Button  style={{ margin: '0 8px' }} onClick={props.history.goBack} >Back</Button>           
              </Form.Item>         
    </Row>
    {joinCondLoader?(
         <Alert message={joincondition} type="info" />
    ):("")}
   
<Row gutter={[16, 16]}>
  <Col span={4}>
  {swtblloader?(
    <Table dataSource={tblColSw} columns={columnssw}   rowSelection={{
          type: selectionType,
          ...rowSelection,
        }}
        pagination={false}
        style={{width:'25%'}}
        bordered
    />
):("")}
  </Col>
  <Col span={4}>
  {gltblloader?(
    <Table dataSource={tblColGl} columns={columns}   rowSelection={{
          type: selectionType,

          ...rowSelection,
        }}
        pagination={false}
        style={{width:'25%'}}
        bordered
    />
):("")}
  </Col>
  
{matchtblloader?(
    <Col span={6}>
 <Table dataSource={matchtbl} columns={columnsmatch} />
  </Col>
):("")}
 
  <Col span={6} />
</Row>
{/* {gltblloader?(
    <Table dataSource={tblCol} columns={columns}   rowSelection={{
          type: selectionType,
          ...rowSelection,
        }}
        pagination={false}
        style={{width:'25%'}}
        bordered
    />
):("")} */}
       
    {/* <Button size="large" type={"primary"} onClick={getinfofromjointables}>Save</Button>  */}

    <Row gutter={[16, 16]}>
  <Col span={4}>
  {matchtblNew?(
                <Table dataSource={matchtbl} columns={columnsmatch}   rowSelection={{
                    type: selectionType,
                    ...rowSelection,
                    }}
                    pagination={false}
                    style={{width:'25%'}}
                    bordered
                />
            ):("")}
  </Col>
  <Col span={4}>
  {npciiss?(
            <Table dataSource={tblColNPCIISS} columns={columnsnpciiss}   rowSelection={{
                type: selectionType,
                ...rowSelection,
                }}
                pagination={false}
                style={{width:'25%'}}
                bordered
            />
        ):("")}
  </Col>
  <Col span={2} />
  {matchtblnpciiss?(
            <Col span={6} >
              <Table dataSource={matchtblnpcidata} columns={columnsmatchwithNPCIISS}   
                style={{width:'25%'}}
                bordered
                pagination={false}
            />
               </Col>
        ):("")}
     
</Row>
{/*------------------------------------------------------------------------------------*/}

{/* 

    <Row gutter={[16, 16]}>
        <Col span={4}>
                    {matchtblNew?(
                <Table dataSource={matchtbl} columns={columnsmatch}   rowSelection={{
                    type: selectionType,
                    ...rowSelection,
                    }}
                    pagination={false}
                    style={{width:'25%'}}
                    bordered
                />
            ):("")}
        </Col>
        <Col span={4} >
        {npciiss?(
            <Table dataSource={tblColNPCIISS} columns={columnsnpciiss}   rowSelection={{
                type: selectionType,
                ...rowSelection,
                }}
                pagination={false}
                style={{width:'25%'}}
                bordered
            />
        ):("")}
        </Col>
        
        {matchtblnpciiss?(
            <Col span={6} >
              <Table dataSource={matchtblnpcidata} columns={columnsmatchwithNPCIISS}   
                pagination={false}
                style={{width:'25%'}}
                bordered
            />
               </Col>
        ):("")}
     
        <Col span={6} />
      </Row> */}
      </Form>
      </Card>
      </Content>
      </Layout>
      </Layout>
      </Layout>
  );
};
export default JoinRuleConfiguration;