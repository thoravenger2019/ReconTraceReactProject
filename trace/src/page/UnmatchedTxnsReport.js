import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import MenuSideBar from './menuSideBar';
import moment from 'moment';
import { FileExcelOutlined } from '@ant-design/icons';
import ExportJsonExcel from 'js-export-excel';
import jsPDF from "jspdf";
import "jspdf-autotable";
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
  Input,
  DatePicker,
  Table,
  Spin,
  Image, 
  Modal,
} from 'antd';
import Title from 'antd/lib/typography/Title';
const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;

const UnmatchedTxnsReport = props => {
  console.log(props)

  const [data, setData] = useState([])
  const [loader, setLoader] = useState(true)
  const [clientid, setClientId]=useState([])
  const [clientData,setClientData]=useState([])
  const [channelData,setChannelData]=useState([])
  const [modeData,setModeData]=useState([])
  const [dispenseSummaryReoprttbldata,setUnmatchedReport]=useState([])
  const [fterminalid,setTerminalId]=useState([])
  const [fRefnum,setRefNumber]=useState([])
  const [glDetailstbl,setGlDetails]=useState([])
  const [swdetailstbl,setSWDetails]=useState([])
  const [ejdetailstbl,setEJDetails]=useState([])
  const [nwDetailstbl,setNWDetails]=useState([])
  const [chanId,setChannelId]=useState([])
  const [modId,setModeID]=useState([])
  const [toDate,setToDate]=useState([])
  const [fromDate,setFromDate]=useState([])

  
  const [selectedFileData, setStateFile] = useState(undefined)
  const [setTerm,setTerminal]=useState(false)
  const [setTxnType,setTxn]=useState(false);
  const [spinLoad,setSpinLoad]=useState(false)
  const [unmatchedtbl,setUnmatched]=useState(false)
  const [exportOpt,setExportOpt]=useState(false)
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    //onDisplayImplortFile();
    onDisplayClientNameList();
  }, [])

  console.log(fterminalid);
  console.log(fRefnum);

  function onChangeClientName(value) {
    console.log(`selected ${value}`);  
    setClientId(value);
    onDisplayChannel(value); 
  }

  function onChangeMode(value) {
    console.log(`selected ${value}`);  
    setModeID(value);
    if(JSON.stringify(value)=="2")  
    {setTxn(true);
       // alert("terminal type")
      setTerminal(true);
    }
    if(JSON.stringify(value)=="3")  
    {setTxn(true);
       // alert("terminal type")
      //setTerminal(true);
    }
   
    //setClientId(value);
   // onDisplayChannel(value); 
  }
  function onChangeChannel(value) {
    console.log(`selected ${value}`);  
    setChannelId(value);
    if(JSON.stringify(value)=="2"){
        alert("txntype")
      setTxn(true);
    }
   
    //setClientId(value);
    ongetModeType(value); 
  }

  const onDisplayClientNameList = async () => {
    try {
        const clientNameResponse = await axios.get(`clientName`);
        console.log(clientNameResponse.data)
        setLoader(false);

        const clientNameN = clientNameResponse.data;
        console.log(clientNameN);
       
        //const role = JSON.parse(roleN.roleNames);
        //console.log(role);
      //  const firstClient=clientNameN[0];
        const clientNameList =clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
        )
        setClientData(clientNameList);

    } catch (e) {
        console.log(e)
    }
};

/*
@GetMapping("/getchanneltyperun/{clientid}")
@GetMapping("/getModeTypeRun/{clientid}/{channelid}")
*/

const onDisplayChannel = async (value) => {
  try {
    const channelResponse = await axios.get(`getchanneltyperun/${value}`);
    console.log(channelResponse.data)
    setLoader(false);

    const channelN = channelResponse.data;
    console.log(channelN);

    const listChannel = channelN.map((item, index) => <Option value={item.channelID} key={index} label={item.channelName}>{item.channelName}</Option>)
    setChannelData(listChannel);

  } catch (e) {
    console.log(e)
  }
};

const ongetModeType = async (value) => {
  try {
      ///alert("client id"+ClientData);
      //alert("channel id"+value);
      const modeResponse = await axios.get(`getModeType/${clientid}/${value}`);
      console.log(modeResponse.data)
      // setModeStatus(true);
      setLoader(false);

      const modeN = modeResponse.data;
      //console.log(modeN);
      //const branch = JSON.parse(modeN.branchName);
      //console.log(branch);
      const listMode = modeN.map((item, index) => <Option value={item.modeid} key={index}>{item.modename}</Option>);
      setModeData(listMode);
      
  } catch (e) {
      console.log(e)
  }
};

// const onRefReport=async()=>{
//   const values = form.getFieldsValue();
//   const gltxnsReport =  axios.get(`gltxndetails/${ReferenceNumber}/${TerminalId}/${clientid}`);
//   console.log(gltxnsReport.data);
//   const swtxnsReport =  axios.get(`swtxndetails/${ReferenceNumber}/${TerminalId}/${clientid}`);
//   console.log("sw txns: ",swtxnsReport.data);
//   const ejtxnsReport =  axios.get(`ejtxndetails/${ReferenceNumber}/${TerminalId}/${clientid}`);
//   console.log("ej txns: ",ejtxnsReport.data);
//   setVisible(true);
//   // const nwtxnsReport =  axios.get(`nwtxndetails/${ReferenceNumber}/${TerminalId}/${values.channelID}/${values.modeID}/${clientid}`);
//   // console.log("nw txns: ",nwtxnsReport.data);

// }


  const onRefReport = async (refid,termid) => {
    try {
      
      console.log(refid);
      console.log(termid);
     // const gldetails;
      const importFileResponse = await axios.get(`gltxndetails/${refid}/${termid}/${clientid}`);
      console.log(importFileResponse.data);
      const gldetails=importFileResponse.data;
      if(JSON.stringify(gldetails)=="[]"){
        //alert("no data recorded");
        const dataNot=gldetails.map((item,index)=>({
          ReferenceNumber: 'nodata'
        }))
        setGlDetails(dataNot);
      }else{
        const dataAll = gldetails.map((item, index) => ({
          CardNumber: item.CardNumber,
          ReferenceNumber: item.ReferenceNumber,
          ResponseCode: item.ResponseCode,
          ReversalFlag: item.ReversalFlag,
          TerminalId: item.TerminalId,
          TxnsAmount: item.TxnsAmount,
          TxnsDateTime: item.TxnsDateTime,
          key: index
        })
        )
        setGlDetails(dataAll);
        //const gldetails="no data found";
      } 
      //setGlDetails(gldetails);
      
      //--------------------------switch data-----------------------------------
      const swtxnsReport = await axios.get(`swtxndetails/${refid}/${termid}/${clientid}`);
      console.log("sw txns: ",swtxnsReport.data);
      const swdetails=swtxnsReport.data;
    
      if(JSON.stringify(swdetails)=="[]"){
       // setSWDetails("no data recorded");
       const  dataAll="no data recored";
      }else{
        const dataAll = swdetails.map((item, index) => ({
          CardNumber: item.CardNumber,
          ReferenceNumber: item.ReferenceNumber,
          ResponseCode: item.ResponseCode,
          ReversalFlag: item.ReversalFlag,
          TerminalId: item.TerminalId,
          TxnsAmount: item.TxnsAmount,
          TxnsDateTime: item.TxnsDateTime,
          key: index
        }))
        setSWDetails(dataAll);
      }
//--------------------------------EJ Details ---------------------------------------------

      const ejtxnsReport = await axios.get(`ejtxndetails/${refid}/${termid}/${clientid}`);
      console.log("ej txns: ",ejtxnsReport.data);
      const ejdetails=ejtxnsReport.data;
    
      if(JSON.stringify(ejdetails)=="[]"){
        //setEJDetails("no data recorded");
       const  dataAll="no data recored";
      }else{
        const dataAll = ejdetails.map((item, index) => ({
          CardNumber: item.CardNumber,
          ReferenceNumber: item.ReferenceNumber,
          ResponseCode: item.ResponseCode,
          ReversalFlag: item.ReversalFlag,
          TerminalId: item.TerminalId,
          TxnsAmount: item.TxnsAmount,
          TxnsDateTime: item.TxnsDateTime,
          ejstatus: item.ejstatus,
          key: index
        }))
        setEJDetails(dataAll);
      }

      const nwtxnsReport = await axios.get(`nwtxndetails/${refid}/${termid}/${chanId}/${modId}/${clientid}`);
      console.log("nw txns: ",nwtxnsReport.data);
      const nwdetails=ejtxnsReport.data;
    
      if(JSON.stringify(nwdetails)=="[]"){
        //setEJDetails("no data recorded");
       const  dataAll="no data recored";
      }else{
        const dataAll = nwdetails.map((item, index) => ({
          CardNumber: item.CardNumber,
          ReferenceNumber: item.ReferenceNumber,
          ResponseCode: item.ResponseCode,
          ReversalFlag: item.ReversalFlag,
          TerminalId: item.TerminalId,
          TxnsAmount: item.TxnsAmount,
          TxnsDateTime: item.TxnsDateTime,
          key: index
        }))
        setNWDetails(dataAll);
      }
      // setLoader(false);
      // const fileN = importFileResponse.data;
      // console.log(fileN);

      // const listFile = fileN.map((item, index) => <Option value={item.id} key={index}>{item.fileType}</Option>)
      // setData(listFile);

      // console.log(dataAll);
      setVisible(true);
    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  console.log(menuData);

  function onChangeTxn(value) {
    console.log(`selected ${value}`);
  }
  
  function onChangeTerm(value) {
    console.log(`selected ${value}`);
  }
  function handleChange(value) {
    console.log(`selected ${value}`);
  }

  const [form] = Form.useForm()
  

  const onFileUpload = async () => {
    try {
      /*const validateFields = await form.validateFields()     
     const values = form.getFieldsValue();
     console.log(values)
    const response = await axios.get(`login1/${values.username}/${values.password}`);
     console.log(response.data)
     setMenuData(response.data)
     props.history.push("/dashboard",response.data)
      value={myStateValue || ''}
*/
     // const validateFields = await form.validateFields();    
     // const values = form.getFieldsValue();
      let currentFile=selectedFileData[0];
      const formData = new FormData();            
      formData.append('file',currentFile);
      console.log(currentFile);
      console.log(formData);
      const response = await axios.post(`importFile`,formData);
    } catch (e) {
      console.log(e)
    }
  }

  const downloadExcel = async() => {
    
    const data=dispenseSummaryReoprttbldata;
    console.log(data);
    var option={};
    let dataTable = [];
    if (data) {
      for (let i in data) {
        if(data){
          let obj = {
          
                      'ChannelName': data [i].ChannelName,
                      'Transaction Mode': data [i] .TransactionMode,
                      'Terminal Id': data [i] .TerminalId,
                      'Reference Number':data[i].ReferenceNumber,
                      'Card Number':data[i].CardNumber,
                      'CustAccountNo':data[i].CustAccountNo,
                      'Txns Amount':data[i].TxnsAmount,
                      'ej status' : data[i].ejstatus,
                      'sw status':data[i].swstatus,
                      'nw status':data[i].nwstatus,
                      'gl status' :data[i].glstatus,
                      'Txns SubType' :data[i].TxnsSubType
          }
          dataTable.push(obj);
        }
      }
    }
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = mm + '-' + dd + '-' + yyyy;
    option.fileName = 'UnmatchedTxnReport'+today
    option.datas=[
      {
        sheetData:dataTable,
        sheetName:'sheet',
               sheetFilter: [  'ChannelName','Transaction Mode','Terminal Id','Reference Number','Card Number','CustAccountNo','Txns Amount','ej status','sw status','nw status','gl status','Txns SubType' ],
               sheetHeader: [  'ChannelName','Transaction Mode','Terminal Id','Reference Number','Card Number','CustAccountNo','Txns Amount','ej status','sw status','nw status','gl status','Txns SubType' ],
      }
    ];
    var toExcel = new ExportJsonExcel(option); 
    toExcel.saveExcel(); 
    }
  

    const downloadPDF = async() => {
      //initialize jsPDF
      const data=dispenseSummaryReoprttbldata;
      console.log(data);
      const doc = new jsPDF('landscape');
      var imgData='./Finallogo.png'
     // doc.setTextColor(255,0,0);
      doc.addImage(imgData, 'PNG', 10, 5, 20, 8,)
      doc.addImage(imgData, 'PNG', 250, 5, 20, 8)
      // define the columns we want and their titles
      const tableColumn = ['Channel Name','Transaction Mode','Terminal Id','Reference Number','Card Number','Account No','Txns Amount','EJ Status','SW Status','NW Status','GL Status','Txn Sub Type'];
      // define an empty array of rows
      const tableRows = [];
      // for each ticket pass all its data into an array
      data.forEach(ticket => {
        const txnData = [
          ticket.ChannelName,
          ticket.TransactionMode,
          ticket.TerminalId,
          ticket.ReferenceNumber,
          ticket.CardNumber,
          ticket.CustAccountNo,
          ticket.TxnsAmount,
          ticket.ejstatus,
          ticket.swstatus,
          ticket.nwstatus,
          ticket.glstatus,
          ticket.TxnsSubType
          // called date-fns to format the date on the ticket
          //format(new Date(ticket.updated_at), "yyyy-MM-dd")
        ];
        // push each tickcet's info into a row
        tableRows.push(txnData);
        console.log(txnData);
      });
    
      // startY is basically margin-top
      doc.autoTable(tableColumn, tableRows, {startY: 30});
      const date = Date().split(" ");
      // we use a date string to generate our filename.
      const dateStr = date[0] + date[1] + date[2] + date[3] + date[4];
      // ticket title. and margin-top + margin-left
      doc.text("Unmatched Transaction Report", 10, 12);
      // we define the name of our PDF file.
      doc.save(`Unmatchedtxnreport_${dateStr}.pdf`);
    
//     const unit = "pt";
//     const size = "A4"; // Use A1, A2, A3 or A4
//     const orientation = ""; 
//     const marginLeft = 40;
//     const doc = new jsPDF('l', unit, size);
//     const dataTable = [];
//     doc.setFontSize(15);
//     const title = "Unmatched Transaction Report";
//     const headers = [['Channel Name','Transaction Mode','Terminal Id','Reference Number','Card Number','Account No','Txns Amount','EJ Status','SW Status','NW Status','GL Status','Txn Sub Type']];
//     const data = dispenseSummaryReoprttbldata;
//     if (data) {
//       for (let i in data) {
//         if(data){
//           let obj = {
          
//                       'ChannelName': data [i].ChannelName,
//                       'Transaction Mode': data [i] .TransactionMode,
//                       'Terminal Id': data [i] .TerminalId,
//                       'Reference Number':data[i].ReferenceNumber,
//                       'Card Number':data[i].CardNumber,
//                       'CustAccountNo':data[i].CustAccountNo,
//                       'Txns Amount':data[i].TxnsAmount,
//                       'ej status' : data[i].ejstatus,
//                       'sw status':data[i].swstatus,
//                       'nw status':data[i].nwstatus,
//                       'gl status' :data[i].glstatus,
//                       'Txns SubType' :data[i].TxnsSubType
//           }
//           dataTable.push(obj);
//         }
//       }
//     }
// console.log(dataTable);
//     console.log(data);
//     let content = {
//       startY: 50,
//       head: headers,
//       body: dataTable
//     };
//     doc.text(title, marginLeft, 40);
//     doc.autoTable(content);
//     doc.save("report.pdf")
    };
    

  const onShowUnmatched=async()=>{
    if(fromDate>toDate){
      alert("ToDate should be greater then FromDate ");
    }
    else{
    const validateFields=await form.validateFields();
    const values = form.getFieldsValue();
    console.log(values); 
    setSpinLoad(true);

    const unmatchedReport = await axios.get(`getunmatchedtxnreport/${values.clientID}/${values.channelID}/${values.modeID}/${values.terminalID}/${fromDate}/${toDate}/${values.txnType}`);
    console.log(unmatchedReport.data)
    setSpinLoad(false);
    const unmatchedReporttbl=unmatchedReport.data;
    if(JSON.stringify(unmatchedReporttbl)=="[]"){
      alert("No data found..!")
      //window.location.reload(false);
    }else{
    const dataAll = unmatchedReporttbl.map((item, index) => ({
      ChannelName: item.ChannelName,
      TransactionMode: item.TransactionMode,
      TerminalId: item.TerminalId,
      ReferenceNumber: item.ReferenceNumber,
      CardNumber:item.CardNumber,
     // GLISSUER:item.GLISSUER,
     CustAccountNo:item.CustAccountNo,
     TxnsAmount:item.TxnsAmount,
     ejstatus:item.ejstatus,
     swstatus:item.swstatus,
     // GLONUS:item.NFSISSUER,
      //GLONUS:item.NFSISSUERDIFF,
      //GLONUS:item.SWISSUER,
      nwstatus:item.nwstatus,
      glstatus:item.glstatus,
      TxnsSubType:item.TxnsSubType,
      key: index,
      size: '15px'
    })
    )
    setUnmatchedReport(dataAll);
    setUnmatched(true);
    setExportOpt(true);
  }
  }
}
 

  const columns = [
    {
      title: 'Channel Name',
      dataIndex: 'ChannelName',
      key: 'ChannelName',
      // render: text => <a>{text}</a>,
    },
    {
      title: 'Transaction Mode',
      dataIndex: 'TransactionMode',
      key: 'TransactionMode',
    },
    {
      title: 'Terminal Id',
      dataIndex: 'TerminalId',
      key: 'TerminalId',
    },
    {
      title: 'Reference Number',
      dataIndex: 'ReferenceNumber',
      key: 'ReferenceNumber',
      // render: (text, record) => <a href={'user/' + record.name}>{text}</a>
      render: (text,record,index) => <a onClick = {
        (e) => {
          console.log("corresponding email is :", record.TerminalId)
          console.log("corresponding email is :", record.ReferenceNumber)
          console.log("corresponding email is :", clientid)
          setTerminalId(record.TerminalId);
          setRefNumber(record.ReferenceNumber);   
          const termid= record.TerminalId;
          const refid= record.ReferenceNumber;
          onRefReport(refid,termid);
        }
      }  /*() => setVisible(true)}*/ >{text}</a>
    },
    {
      title: 'Card Number',
      dataIndex: 'CardNumber',
      key: 'CardNumber',
    },
    {
      title: 'AccountNo',
      dataIndex: 'CustAccountNo',
      key: 'CustAccountNo',
    },    
    {
      title: 'Amount',
      dataIndex: 'TxnsAmount',
      key: 'TxnsAmount',
    },
    {
      title: 'ej status',
      dataIndex: 'ejstatus',
      key: 'ejstatus',
    },
    {
      title: 'sw status',
      dataIndex: 'swstatus',
      key: 'swstatus',
    },
    {
      title: 'nw status',
      dataIndex: 'nwstatus',
      key: 'nwstatus',
    },
    {
      title: 'gl status',
      dataIndex: 'glstatus',
      key: 'glstatus',
    },
    {
      title: 'Txns Type',
      dataIndex: 'TxnsSubType',
      key: 'TxnsSubType',
    }
  ];


  const ejcolumns = [
    {
      title: 'TerminalId',
      dataIndex: 'TerminalId',
      key: 'TerminalId',
      // render: text => <a>{text}</a>,
    },
    {
      title: 'TxnsDateTime',
      dataIndex: 'TxnsDateTime',
      key: 'TxnsDateTime',
    },
    {
      title: 'CardNumber',
      dataIndex: 'CardNumber',
      key: 'CardNumber',
    },
    {
      title: 'ReferenceNumber',
      dataIndex: 'ReferenceNumber',
      key: 'ReferenceNumber',
    },
    {
      title: 'TxnsAmount',
      dataIndex: 'TxnsAmount',
      key: 'TxnsAmount',
    },
    {
      title: 'ResponseCode',
      dataIndex: 'ResponseCode',
      key: 'ResponseCode',
    },    
    {
      title: 'ej status',
      dataIndex: 'ejstatus',
      key: 'ejstatus',
    }
  ];

  const swcolumns = [
    {
      title: 'TerminalId',
      dataIndex: 'TerminalId',
      key: 'TerminalId',
      // render: text => <a>{text}</a>,
    },
    {
      title: 'TxnsDateTime',
      dataIndex: 'TxnsDateTime',
      key: 'TxnsDateTime',
    },
    {
      title: 'CardNumber',
      dataIndex: 'CardNumber',
      key: 'CardNumber',
    },
    {
      title: 'ReferenceNumber',
      dataIndex: 'ReferenceNumber',
      key: 'ReferenceNumber',
    },
    {
      title: 'TxnsAmount',
      dataIndex: 'TxnsAmount',
      key: 'TxnsAmount',
    },
    {
      title: 'ResponseCode',
      dataIndex: 'ResponseCode',
      key: 'ResponseCode',
    },    
    {
      title: 'ReversalFlag',
      dataIndex: 'ReversalFlag',
      key: 'ReversalFlag',
    }
  ];


  const nwcolumns = [
    {
      title: 'TerminalId',
      dataIndex: 'TerminalId',
      key: 'TerminalId',
      // render: text => <a>{text}</a>,
    },
    {
      title: 'TxnsDateTime',
      dataIndex: 'TxnsDateTime',
      key: 'TxnsDateTime',
    },
    {
      title: 'CardNumber',
      dataIndex: 'CardNumber',
      key: 'CardNumber',
    },
    {
      title: 'ReferenceNumber',
      dataIndex: 'ReferenceNumber',
      key: 'ReferenceNumber',
    },
    {
      title: 'TxnsAmount',
      dataIndex: 'TxnsAmount',
      key: 'TxnsAmount',
    },
    {
      title: 'ResponseCode',
      dataIndex: 'ResponseCode',
      key: 'ResponseCode',
    },    
    {
      title: 'ReversalFlag',
      dataIndex: 'ReversalFlag',
      key: 'ReversalFlag',
    }
  ];

  const glcolumns = [
    {
      title: 'TerminalId',
      dataIndex: 'TerminalId',
      key: 'TerminalId',
      // render: text => <a>{text}</a>,
    },
    {
      title: 'TxnsDateTime',
      dataIndex: 'TxnsDateTime',
      key: 'TxnsDateTime',
    },
    {
      title: 'CardNumber',
      dataIndex: 'CardNumber',
      key: 'CardNumber',
    },
    {
      title: 'ReferenceNumber',
      dataIndex: 'ReferenceNumber',
      key: 'ReferenceNumber',
    },
    {
      title: 'TxnsAmount',
      dataIndex: 'TxnsAmount',
      key: 'TxnsAmount',
    },
    {
      title: 'ResponseCode',
      dataIndex: 'ResponseCode',
      key: 'ResponseCode',
    },    
    {
      title: 'ReversalFlag',
      dataIndex: 'ReversalFlag',
      key: 'ReversalFlag',
    }
  ];

  function onChangeToDate(dateString) {
    console.log(dateString);
    setToDate(dateString);
  }
  
  function onChangefromDate(dateString) {
    console.log(dateString);
    setFromDate(dateString);
  }

  return (

<Layout>
      <Header style={{ padding: "20px" }}>
        <Avatar shape="square" style={{ float: "right" }} size="default" src="./max.png" />
        <Title level={3} style={{ color: "white" }}>Trace</Title>
      </Header>
      <Layout>
        <MenuSideBar menuData={menuData} />
        <Layout style={{ height: "100vh", backgroundColor: "white" }}>
          <Content>
            <Card title="Unmatched Txns Report" bordered={false} style={{ width: 1500 }}>
              <Form initialValues={{ remember: true }} layout={"vertical"} size={"large"} form={form}>
                <Row  gutter={8} >
                    <Col span={4}>
                    <Form.Item label="Bank Name" name="clientID" 
                    rules={[{ required: true, message: "Bank name required" }]}>
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeClientName} 
                        >
                              {clientData}
                           </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={4}>
                      
                      <Form.Item label="Channel Type" name="channelID" >
                        <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeChannel}>
                              {channelData}
                        </Select>                  
                    </Form.Item>

                    </Col>
                    <Col span={4}>
                      <Form.Item label="Mode Type" name="modeID" >
                          <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeMode}>
                                {modeData}
                          </Select>                  
                      </Form.Item>
                    </Col>
                    <Col span={4}>
                    {setTxnType?(
                      <Form.Item label="Txn Type" name="txnType" >
                       
                           <Select defaultValue="--select--" style={{ width: 200 }} onChange={onChangeTxn}>
                           <Option value="0">--Select--</Option>
                           <Option value="Withdrawal">Withdrawal</Option>
                           <Option value="Deposit">Deposit</Option>
                      </Select> 
                      </Form.Item> 
                        ):("")}                                         
                    </Col>
                    <Col span={4}>
                    {setTerm?(
                      <Form.Item label="Terminal" name="terminalID" >
                        <Select defaultValue="ALL" style={{ width: 200 }} onChange={onChangeTerm}>
                          <Option value="0">ALL</Option>
                          </Select> 
                          </Form.Item>):("")}
                      </Col>
                </Row>
                <Row  gutter={4} style={{ height: '50%' }}  >
                <Col span={4}>
                <Form.Item label="from Date" name="fromDateTxns" style={{width: 320}}
                rules={[{ required: true, message: "From Date required" }]}>    
                 
                  {/* <DatePicker  format={dateFormat} style={{width:320 }} /> */}
                  {/* <Input type={"date"} onChange={onfromDateChange} id={"fromDateTxns"} onkeydown="return false"></Input> */}
                  {/* <input type="date" onkeydown="return false" /> */}
                  <DatePicker style={{ width: 200 }} onChange={(date, dateString) => onChangefromDate(dateString)} size={"large"}/>
                </Form.Item>
                </Col>
                <Col span={2}>
                <Form.Item label="to Date" name="toDateTxns"  style={{width: 320}}
                 rules={[{ required: true, message: "To Date required" }]}> 
                  <DatePicker style={{ width: 200 }} onChange={(date, dateString) => onChangeToDate(dateString)} size={"large"} />
                  </Form.Item>
                  </Col>
                </Row>              
                <Row  gutter={8}>  
                {exportOpt?(
                  <Form.Item label=" " name="">             
                     <Button type={"primary"} size={"large"} style={{width:'100px'}} onClick={onShowUnmatched}>Show</Button>                       
                     {/* <Button style={{margin: '0 18px'}} shape="circle-outline" onClick={downloadExcel}  icon={ <FileExcelOutlined size={"large"}style={{background:'green'}}/>}   size={"large"}/> */}                   
                     <a style={{margin: '0 18px'}}><Avatar  shape ="square"  size="large" src="./export-to-excel.png" onClick={downloadExcel}/></a>
                     <a style={{margin: '0 2px'}}><Avatar  shape ="square"  size="large" src="./pdf.png" onClick={downloadPDF}/></a>
                     {spinLoad?(<Spin style={{ margin: '0 38px', color: 'black' }} size="large" />):("") }         
                  </Form.Item>   
                ):(
                  <Form.Item label=" " name="">             
                     <Button type={"primary"} size={"large"} style={{width:'100px'}} onClick={onShowUnmatched}>Show</Button>                       
                     {/* <Button style={{margin: '0 18px'}} shape="circle-outline" onClick={downloadExcel}  icon={ <FileExcelOutlined size={"large"}style={{background:'green'}}/>}   size={"large"}/> */}                   
                     {spinLoad?(<Spin style={{ margin: '0 38px', color: 'black' }} size="large" />):("") }         
                  </Form.Item>
                )}
                             
                </Row>
              </Form>
              {unmatchedtbl?(<Table columns={columns} dataSource={dispenseSummaryReoprttbldata} bordered/>):("")}
              <Modal
                  title="Transaction ID Details"
                  centered
                  visible={visible}
                  onOk={() => setVisible(false)}
                  onCancel={() => setVisible(false)}
                  width={1500}
                >
                  <b><p style={{textAlign:"center", backgroundColor:"#87e8de"} } size="large">EJ DETAILS</p></b>
                  <Table style={{backgroundColor:'blue'}} columns={ejcolumns} dataSource={ejdetailstbl} pagination={false} bordered></Table>
                  <b><p style={{textAlign:"center",backgroundColor:"#87e8de"} } size="large">SW DETAILS</p></b>
                  <Table columns={swcolumns} dataSource={swdetailstbl} pagination={false}  bordered></Table>
                  <b><p style={{textAlign:"center",backgroundColor:"#87e8de"} } size="large">NW DETAILS</p></b> 
                  <Table columns={nwcolumns} dataSource={nwDetailstbl} pagination={false}  bordered></Table>
                   <b><p style={{textAlign:"center",backgroundColor:"#87e8de"} } size="large">GL DETAILS</p></b>
                  <Table columns={glcolumns} dataSource={glDetailstbl} pagination={false} bordered></Table> 
              </Modal>
            </Card>
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};
export default UnmatchedTxnsReport;