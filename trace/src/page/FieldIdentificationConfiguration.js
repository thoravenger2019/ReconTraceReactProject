import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import axios, { axiosGet } from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
    Form,
    Input,
    Button,
    Select,
    Card,
    Row,
    Col,
    Checkbox,
    Layout,
    Avatar,
} from 'antd';
import Password from 'antd/lib/input/Password';
import { LayoutContext } from 'antd/lib/layout/layout';
import Title from 'antd/lib/typography/Title';
const { Header, Footer, Sider, Content } = Layout;
const FormItem = Form.Item;
const { Option } = Select;

const FieldIdentificationConfiguration = props => {
    //console.log(props)  
    const [clientID, setClientID] = useState([])
    const [channeldata, setChannelData] = useState([])
    const [ClientData, setClientData] = useState([])
    const [formatIDData, setFormatIDData] = useState([])
    const [vendorData, setVendorData] = useState([])
    const [modeData,setModeData]=useState([])
    const [vendorId,setVendorID]=useState([])
    const [schannelID,setChannelID]=useState([])
    const [smodeID,setModeID]=useState([])
    const [loader, setLoader] = useState(true) 
    const [formLoder,setFormLoader]=useState(false)
    console.log(clientID);

    useEffect(() => {   
        // onDisplayUserRole();
        //onDisplayChannel();
        //onDisplayBranch();
        onDisplayClientNameList();
    }, [])

    const onDisplayClientNameList = async () => {
        try {
            const clientNameResponse = await axios.get(`clientName`);
            //console.log(clientNameResponse.data)
            setLoader(false);

            const clientNameN = clientNameResponse.data;
            //console.log(clientNameN);
            const clientNameList = clientNameN.map((item, index) =>
                <Option value={item.id} key={index}>{item.clientNameList}
                </Option>
            )
            setClientData(clientNameList);

        } catch (e) {
            console.log(e)
        }
    };

    const ongetfilevendordetails = async (value) => {
        try {
          const vendorResponse = await axios.get(`getfilevendordetails/${value}`);
          //console.log(vendorResponse.data)
          setLoader(false);
    
          const vendorN = vendorResponse.data;
    
          const listVendor = vendorN.map((item, index) => <Option value={item.vendorid} key={index}>{item.vendorname}</Option>)
          setVendorData(listVendor);
    
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
    


    const onGetFormatId = async () => {
        try {
            const formatId = await axios.get(`getformatid/${clientID}/${vendorId}`);
           // console.log(formatId.data)
            setLoader(false);

        const formatIdN = formatId.data;
            //console.log(formatIdN);

            //const role = JSON.parse(roleN.roleNames);
            //console.log(role);

            const listFormatId = formatIdN.map((item, index) =>item.formatid)
            setFormatIDData(listFormatId);

        } catch (e) {
            console.log(e)
        }
    };


    const ongetchannelmodeinfo = async (value) => {
        try {
            ///alert("client id"+ClientData);
            alert("channel id"+value);           
            const modeResponse = await axios.get(`getchannelmodeinfo/${clientID}/${value}`);
            //console.log(zxxmodeResponse.data)
            setLoader(false);

            const modeN = modeResponse.data;
            //console.log(modeN);
            //const branch = JSON.parse(modeN.branchName);
            //console.log(branch);
            const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            setModeData(listMode);
            onGetFormatId();
        } catch (e) {
            console.log(e)
        }
    };

    const ongetfieldidentification= async (value) => {
        try {
         //  alert("chnnel"+schannelID);
           //alert("mode"+smodeID);
          // alert("formatid"+formatIDData);
            const getFieldResponse = await axios.get(`getfieldidentification/${clientID}/${vendorId}/${schannelID}/${value}/${formatIDData}`);
            console.log(getFieldResponse.data)
            const fieldData=getFieldResponse.data;
            if(fieldData!=null){
                setFormLoader(true);
            }
          

           /* const modeN = modeResponse.data;
            //console.log(modeN);
            //const branch = JSON.parse(modeN.branchName);
            //console.log(branch);
            const listMode = modeN.map((item, index) => <Option value={item.ModeID} key={index}>{item.TransactionMode}</Option>);
            setModeData(listMode);
            onGetFormatId();*/
        } catch (e) {
            console.log(e)
        }
    };

    const menuData = props.location.state;
   // console.log(menuData);

    const [form] = Form.useForm()

    const addFiledDetails = async () => {
        try {
            const validateFields = await form.validateFields();
            const values = form.getFieldsValue();
            console.log(values);
            ///addfieldconfig/{}/{P_VENDORID}/{P_FORMATID}/{P_TERMINALCODE}/{}/{}/{}/{}/{}/{}/{}/{}/{}/{}/  
            ///var finalformatIDData=(formatIDData)   
           
            const tid=values.P_TERMINALCODE;
            const bid=values.P_BINNO;
            const acqid=values.P_ACQUIRERID;
            const rev1=values.P_REVCODE1;
            const rev2=values.P_REVCODE2;
            const revtype=values.P_REVTYPE;
            const reventry=values.P_REVENTRY;
            const txndt=values.P_TXNDATETIME;
            const txnvaldt=values.P_TXNVALUEDATETIME;
            const txnpstdt=values.P_TXNPOSTDATETIME;
            const atmtype=values.P_ATMTYPE;
            const posttype=values.P_POSTYPE;
            const ecomtype=values.P_ECOMTYPE;
            const impstype=values.P_IMPSTYPE;
            const upitype=values.P_UPITYPE;
            const microtype=values.P_MICROATMTYPE;
            const mobrech=values.P_MOBILERECHARGETYPE;
            const deposit=values.P_DEPOSIT;
            const baleq=values.P_BALENQ;
            const ministmt=values.P_MINISTATEMENT;
            const pinchnge=values.P_PINCHANGE;
            const chqbkeq=values.P_CHEQUEBOOKREQ;
            const respcd1=values.P_RESPCODE1;
            const respcd2=values.P_RESPCODE2;
            const resptyp=values.P_RESPTPE;
            const eodcd=values.P_EODCODE;
            const offlinecd=values.P_OFFLINECODE;
            const dbcd=values.P_DEBITCODE;
            const crcd=values.P_CREDITCODE;

            const formDataTerminal=new FormData();
            formDataTerminal.append('P_TERMINALCODE',tid);
            formDataTerminal.append('P_BINNO',bid);
            formDataTerminal.append('P_ACQUIRERID',acqid);
            formDataTerminal.append('P_REVCODE1',rev1);
            formDataTerminal.append('P_REVCODE2',rev2);
            formDataTerminal.append('P_REVTYPE',revtype);
            formDataTerminal.append('P_REVENTRY',reventry);
            formDataTerminal.append('P_TXNDATETIME',txndt);
            formDataTerminal.append('P_TXNVALUEDATETIME',txnvaldt);
            formDataTerminal.append('P_TXNPOSTDATETIME',txnpstdt);
            formDataTerminal.append('P_ATMTYPE',atmtype);
            formDataTerminal.append('P_POSTYPE',posttype);
            formDataTerminal.append('P_ECOMTYPE',ecomtype);
            formDataTerminal.append('P_IMPSTYPE',impstype);
            formDataTerminal.append('P_UPITYPE',upitype);
            formDataTerminal.append('P_MICROATMTYPE',microtype);
            formDataTerminal.append('P_MOBILERECHARGETYPE',mobrech);
            formDataTerminal.append('P_DEPOSIT',deposit);
            formDataTerminal.append('P_BALENQ',baleq);
            formDataTerminal.append('P_MINISTATEMENT',ministmt);
            formDataTerminal.append('P_PINCHANGE',pinchnge);
            formDataTerminal.append('P_CHEQUEBOOKREQ',chqbkeq);
            formDataTerminal.append('P_RESPCODE1',respcd1);
            formDataTerminal.append('P_RESPCODE2',respcd2);
            formDataTerminal.append('P_RESPTPE',resptyp);
            formDataTerminal.append('P_EODCODE',eodcd);
            formDataTerminal.append('P_OFFLINECODE',offlinecd);
            formDataTerminal.append('P_DEBITCODE',dbcd);
            formDataTerminal.append('P_CREDITCODE',crcd);
           

            //const response = await axios.get(`addfieldconfig/${values.P_CLIENTID}/${values.P_VENDORID}/${formatIDData}/${values.P_TERMINALCODE}/${values.P_BINNO}/${values.P_ACQUIRERID}/${values.P_REVCODE1}/${values.P_REVCODE2}/${values.P_REVTYPE}/${values.P_REVENTRY}/${values.P_TXNDATETIME}/${values.P_TXNVALUEDATETIME}/${values.P_TXNPOSTDATETIME}/${values.P_ATMTYPE}/${values.P_POSTYPE}/${values.P_ECOMTYPE}/${values.P_IMPSTYPE}/${values.P_UPITYPE}/${values.P_MICROATMTYPE}/${values.P_MOBILERECHARGETYPE}/${values.P_DEPOSIT}/${values.P_BALENQ}/${values.P_MINISTATEMENT}/${values.P_PINCHANGE}/${values.P_CHEQUEBOOKREQ}/${values.P_RESPCODE1}/${values.P_RESPCODE2}/${values.P_RESPTPE}/${values.P_EODCODE}/${values.P_OFFLINECODE}/${values.P_DEBITCODE}/${values.P_CREDITCODE}`);
           const response = await axios.post(`addfieldconfig/${values.P_CLIENTID}/${values.P_VENDORID}/${formatIDData}/${schannelID}`,formDataTerminal);
           //,values.P_BINNO,values.P_ACQUIRERID,values.P_REVCODE1,values.P_REVCODE2,values.P_REVTYPE,values.P_REVENTRY,values.P_TXNDATETIME,values.P_TXNVALUEDATETIME,values.P_TXNPOSTDATETIME,values.P_ATMTYPE,values.P_POSTYPE,values.P_ECOMTYPE,values.P_IMPSTYPE,values.P_UPITYPE,values.P_MICROATMTYPE,values.P_MOBILERECHARGETYPE,values.P_DEPOSIT,values.P_BALENQ,values.P_MINISTATEMENT,values.P_PINCHANGE,values.P_CHEQUEBOOKREQ,values.P_RESPCODE1,values.P_RESPCODE2,values.P_RESPTPE,values.P_EODCODE,values.P_OFFLINECODE,values.P_DEBITCODE,values.P_CREDITCODE
          console.log(response.data)
          var fieldResp=response.data;
          var fieldData=fieldResp.map((item,index)=>item.status) 

            if (JSON.stringify(fieldData) === '[Save]') {
                alert("Added successfully");
            }
            else {
                alert("already exist");
            }
            //props.history.push("/AddUser",response.data)
        } catch (e) {
            console.log(e)
        }
    }

    const [componentSize, setComponentSize] = useState('small');

  
    function onChange(checkedValues) {
        console.log('checked = ', checkedValues);
    }
    function onChangeClientName(value) {
        console.log(`selected client ${value}`);
        setClientID(value)
       // ongetchannelmodeinfo(value);
        ongetfilevendordetails(value);
        onGetChannelDetails(value);
    }

    function onChangeMode(value) {
        console.log(`selected mode ${value}`);      
        setModeID(value);
        ongetfieldidentification(value);
    }

    function onChangeChannel(value) {
        console.log(`selected channel ${value}`);
        alert(value);
        ongetchannelmodeinfo(value);
        setChannelID(value);
    }

    function onChangeVendor(value) {
        console.log(`selected vendor ${value}`);
        setVendorID(value);     
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
                        <Card title="Field Identification Configuration" bordered={false} style={{ width: 1400 }} >
                        
                            <Form initialValues={{ remember: true }} layout={"vertical"} form={form} size={"large"}>
                                <Row gutter={[32, 32]} layout="inline">
                                    <Col span={12}><b>
                                        <Form.Item
                                            label="Client Name"
                                            name="P_CLIENTID"
                                            rules={[{ required: true, message: 'required' }]}>
                                            <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeClientName}>
                                                {ClientData}
                                            </Select>
                                        </Form.Item>
                                    </b></Col>
                                    <Col span={12}><b>
                                        <Form.Item
                                            label="Vendor Name"
                                            name="P_VENDORID"
                                            rules={[{ required: true, message: 'required' }]}>
                                            <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeVendor}>
                                                {vendorData}
                                            </Select>
                                        </Form.Item>
                                    </b></Col>
                                </Row>
                                <Row gutter={[32, 32]} layout="inline">
                                    <Col span={12}><b>
                                        <Form.Item
                                            label="Channel Name"
                                            name="P_Channel"
                                            rules={[{ required: true, message: 'required' }]}>
                                            <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeChannel}>
                                                {channeldata}                                     
                                            </Select>
                                        </Form.Item>
                                    </b></Col>
                                    <Col span={12}><b>
                                        <Form.Item
                                            label="Mode Name"
                                            name="P_Mode"
                                            rules={[{ required: true, message: 'required' }]}>
                                            <Select defaultValue="--select--" style={{ width: 500 }} onChange={onChangeMode}>
                                                {modeData}
                                            </Select>
                                        </Form.Item>
                                    </b></Col>
                                </Row>
                                <Row>
                                    <Form.Item
                                        label="Format No:"
                                        name="P_FORMATID"
                                    //rules={[{ required: true, message: 'required' }]}
                                    >
                                       {formatIDData}
                                    </Form.Item>
                                </Row>
                            </Form>
                            <Card bordered={true} style={{ width: 1400 }}>
                                <Form title="" layout={"horizontal"} size={"large"} form={form}>
                                    <Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Terminal Code"
                                                name="P_TERMINALCODE"
                                            >
                                                <Input size={"large"} placeholder="Enter terminal Prefix" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="BIN No"
                                                name="P_BINNO"
                                            >
                                                <Input size={"large"} placeholder="Enter bin No" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Acquirer ID"
                                                name="P_ACQUIRERID"
                                            >
                                                <Input size={"large"} placeholder="Enter Acquirer ID" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                    <Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Reversal Code"
                                                name="P_REVCODE1"
                                            >
                                                <Input size={"large"} placeholder="Enter Reversal Code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Reversal Code"
                                                name="P_REVCODE2"
                                            >
                                                <Input size={"large"} placeholder="Enter Reversal Code(if req)" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ReversalType"
                                                name="P_REVTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter ReversalType" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                    <Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Reversal Entry"
                                                name="P_REVENTRY"
                                            >
                                                <Select size={"large"} placeholder="Enter Reversal Entry" >
                                                    <Option value="1">1</Option>
                                                    <Option value="2">2</Option>
                                                </Select>
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Debit Type"
                                                name="P_DEBITCODE"
                                            >
                                                <Input size={"large"} placeholder="Enter Debit Type" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Credit Type"
                                                name="P_CREDITCODE"
                                            >
                                                <Input size={"large"} placeholder="EnterCredit Type" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                    <Row gutter={8}>
                                        <Col span={8}>
                                            <b>
                                            <Form.Item
                                                label="Txn DateTime"
                                                name="P_TXNDATETIME"
                                            >
                                                <Input size={"large"} placeholder="Enter Txn DateTime" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ValueDateTime"
                                                name="P_TXNVALUEDATETIME"
                                            >
                                                <Input size={"large"} placeholder="Enter ValueDateTime" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="Post DateTime"
                                                name="P_TXNPOSTDATETIME"
                                            >
                                                <Input size={"large"} placeholder="Enter Post DateTime" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row><Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ATM"
                                                name="P_ATMTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter ATM code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="POS"
                                                name="P_POSTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter POS code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ECOM"
                                                name="P_ECOMTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter ECOM Code" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                    <Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="IMPS"
                                                name="P_IMPSTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter IMPS Code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="UPI"
                                                name="P_UPITYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter UPI code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="MicroATM"
                                                name="P_MICROATMTYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter MicroATM code" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row><Row gutter={8} >
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="MobileRecharge"
                                                name="P_MOBILERECHARGETYPE"
                                            >
                                                <Input size={"large"} placeholder="Enter MobileRecharge Code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="CashDeposit"
                                                name="P_DEPOSIT"
                                            >
                                                <Input size={"large"} placeholder="Enter CashDeposit  code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="BalanceEnquiry"
                                                name="P_BALENQ"
                                            >
                                                <Input size={"large"} placeholder="Enter BalanceEnquiry code" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row><Row gutter={8} >

                                        <Col span={8}><b>
                                            <Form.Item
                                                label="MiniStatement"
                                                name="P_MINISTATEMENT"
                                            >
                                                <Input size={"large"} placeholder="Enter MiniStatement Code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="PinChange"
                                                name="P_PINCHANGE"
                                            >
                                                <Input size={"large"} placeholder="Enter PinChange Code" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ChequeBookReq"
                                                name="P_CHEQUEBOOKREQ"
                                            >
                                                <Input size={"large"} placeholder="Enter ChequeBookReq Code" />
                                            </Form.Item>
                                        </b></Col>

                                    </Row>
                                    <Row gutter={8} >

                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ResponseCode"
                                                name="P_RESPCODE1"
                                            >
                                                <Input size={"large"} placeholder="Enter ResponseCode" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ResponseCode"
                                                name="P_RESPCODE2"
                                            >
                                                <Input size={"large"} placeholder="Enter ResponseCode" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="ResponseType"
                                                name="P_RESPTPE"
                                            >
                                                <Input size={"large"} placeholder="Enter ResponseType" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                    <Row gutter={8} >

                                        <Col span={8}><b>
                                            <Form.Item
                                                label="EODCode"
                                                name="P_EODCODE"
                                            >
                                                <Input size={"large"} placeholder="Enter EODCode" />
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="TxnAmountIsDecimal"
                                                name="TxnAmountIsDecimal"
                                            >
                                                <Select size={"large"}>
                                                    <Option value={"1"}>true</Option>
                                                    <Option value={"2"}>false</Option>
                                                </Select>
                                            </Form.Item>
                                        </b></Col>
                                        <Col span={8}><b>
                                            <Form.Item
                                                label="OfflineCode"
                                                name="P_OFFLINECODE"
                                            >
                                                <Input size={"large"} placeholder="Enter OfflineCode" />
                                            </Form.Item>
                                        </b></Col>
                                    </Row>
                                </Form>

                            </Card>
                            <Row>

                                <Form.Item>
                                    <Button type={"primary"} size={"large"} style={{ width: '100px' }} onClick={addFiledDetails}>Submit</Button>
                                    <Button type={"danger"} size={"large"} style={{ margin: '55px' }, { width: '100px' }} onClick={props.history.goBack} >Back</Button>
                                </Form.Item>

                            </Row>

                        </Card>
                    </Content>
                </Layout>
            </Layout>
        </Layout>
    );
};
export default FieldIdentificationConfiguration;