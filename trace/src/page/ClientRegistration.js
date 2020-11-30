import React, { useState, useEffect } from 'react';
//import ReactDOM from 'react-dom';
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
    Layout,
    Avatar,
    Table,
    Space,

} from 'antd';
import {
    EditOutlined,
  } from '@ant-design/icons';
import Title from 'antd/lib/typography/Title';
const { Header, Content } = Layout;
//const FormItem = Form.Item;
const { Option } = Select;

const ClientRegistration = props => {
    console.log(props)
    const [domaindata, setDomainData] = useState([])
    const [clientdata, setClientData] = useState([])
    const [moduledata, setModuleData] = useState([])
    const [loader, setLoader] = useState(false)
    const [clientGetData,setClientTableData]=useState()
    const [editingKey, setEditingKey] = useState('');
    
    console.log(clientGetData)
    useEffect(() => {
        onDisplayDomainTypeList();
        onDisplayModuleTypeList();
        onDisplayClientNameList();
        //onDisplayChannel();
        //onDisplayBranch();
    }, [])

    const onDisplayDomainTypeList = async () => {
         try {
             const domainResponse = await axios.get(`domainTypeList`);
             console.log(domainResponse.data)
             setLoader(false);
 
             const domainN = domainResponse.data;
             console.log(domainN);
 
             //const role = JSON.parse(roleN.roleNames);
             //console.log(role);
            let abc=1;
             const listDomain = domainN.map((item, abc) => <Option value={item.id} key={abc}>{item.domaintype}</Option>)
             setDomainData(listDomain);
             props.history.push("/AddClient",listDomain)
         } catch (e) {
             console.log(e)
         }
    };
 const onDisplayModuleTypeList = async () => {
         try {
             const moduleResponse = await axios.get(`moduleTypeList`);
             console.log(moduleResponse.data)
             setLoader(false);
 
             const moduleN = moduleResponse.data;
             console.log(moduleN);
 
             //const role = JSON.parse(roleN.roleNames);
             //console.log(role);
             let index=1;
             const listModule = moduleN.map((item, index) => <Option value={item.id} key={index}>{item.moduletype}</Option>)
             setModuleData(listModule);
 
         } catch (e) {
             console.log(e)
         }
    };

     const onDisplayClientNameList = async () => {
         try {
             const clientNameResponse = await axios.get(`clientName`);
             console.log(clientNameResponse.data)
             setLoader(false);

             const clientNameN = clientNameResponse.data;
             console.log(clientNameN);
            
             //const role = JSON.parse(roleN.roleNames);
             //console.log(role);
 
             const clientNameList =clientNameN.map((item, index) =>
             <Option value={item.id} key={index}>{item.clientNameList}
             </Option>
             )
             setClientData(clientNameList);
 
         } catch (e) {
             console.log(e)
         }
    };

    const columns = [
        {
          title: 'Client Code',
          dataIndex: 'ClientCode',
          key: 'ClientCode',
         // width: '10%',
          render: text => <a>{text}</a>,
        },
        {
          title: 'Client Name',
          dataIndex: 'ClientName',
          key: 'ClientName',
        //  width: '10%',
        },
          {
          title: 'Client Address',
          dataIndex: 'ClientAddress',
          key: 'ClientAddress',
         // width: '10%',
        },
        {
          title: 'Contact No.',
          dataIndex: 'ContactNo',
          key: 'ContactNo',
        },
              {
          title: 'Email ID',
          dataIndex: 'EmailID',
          key: 'EmailID',
        },
        {
            title: 'Concern Person Name',
            dataIndex: 'ConcernPersonName',
            key: 'ConcernPersonName',
          },
          {
            title: 'Concern Person Contact No',
            dataIndex: 'ConcernPersonContactNo',
            key: 'ConcernPersonContactNo',
          },
          {
            title: 'Concern Person Email ID',
            dataIndex: 'ConcernPersonEmailID',
            key: 'ConcernPersonEmailID',
          },
          {
            title: 'FTP Username',
            dataIndex: 'FTPUsername',
            key: 'FTPUsername',
          },
          {
            title: 'FTP IP',
            dataIndex: 'FTPIP',
            key: 'FTPIP',
          },
          {
            title: 'Terminal Count',
            dataIndex: 'TerminalCount',
            key: 'TerminalCount',
          },
          {
            title: 'User Limit',
            dataIndex: 'UserLimit',
            key: 'UserLimit',
          },
          {
            title: 'Report Cut-Off Time',
            dataIndex: 'ReportCutOffTime',
            key: 'ReportCutOffTime',
          },
          {
            title: 'Client Logo',
            dataIndex: 'ClientLogo',
            key: 'ClientLogo',
          },
          {
            title: 'Color Code',
            dataIndex: 'ColorCode',
            key: 'ColorCode',
          },
          {
            title: 'Update',
            dataIndex: 'update',
            key: 'update',
            render: (text, record) => (
                
            <Space size="middle">
            
                <a onClick={() => { onAccess(record) }}><EditOutlined /></a>
            </Space>
            ),
            },
      ];
      const menuData = props.location.state;
      console.log(menuData);
    const onAccess = (record) => {

      

        //props.history.push("/AccessRight",roleId)
        props.history.push("/UpdateClient", {record,menuData} )
        //props.history.push("/AccessRight", roleId) 
        console.log(record)
  }
  const cancel = () => {
    setEditingKey('');
  };

 const onClientSearch = async () => {
   try {
        setLoader(true);
        const validateFields = await form.validateFields()     
        const values = form.getFieldsValue();
        console.log(values)
        const clientResponse = await axios.get(`getclientmastermodeget/${values.clientid}/${"GET"}/${values.domainid}/${values.moduleid}`);
        console.log(clientResponse.data)
        const clientData=clientResponse.data;
        //props.history.push("/AddClient",clientData)
        const clientN = clientResponse.data;
        console.log(clientN);
      //const vendor = JSON.parse(vendorN.roleNames);
      //console.log(role);

        const dataAll = clientN.map((item, index) => ({
        /*
          ClientID: 22
          CounrtyID: 110
          CreatedBy: "MUSTAFA"
          CreatedOn: 1596005806000
          CurrencyID: 110
          DomainID: 1
          FTPPort: "9090"
          IsActive: 1
          IsBank: 1
          ModuleID: 1*/
        ClientCode: item.ClientCode,
        ClientName: item.ClientName,
        Country: item.Country,
        ClientAddress: item.Address,
        ContactNo:item.ContactNo,
        EmailID:item.EmailID,
        ConcernPersonName:item.ConcernPerson,
        ConcernPersonContactNo:item.CPContactNo,
        ConcernPersonEmailID:item.CPEmailID,
        FTPUsername:item.FTPUserName,
        FTPIP:item.FTP_IP,
        TerminalCount:item.TerminalCount,
        UserLimit:item.UserLimit,
        ReportCutOffTime:item.ReportCutoffTime,
        ClientLogo:item.logoName,
        ColorCode:item.clientColor,
        ClientID:item.ClientID,
        CounrtyID:item.CounrtyID,
        CurrencyID:item.CurrencyID,
        DomainID:item.DomainID,
        FTPPort:item.FTPPort,
        IsActive:item.IsActive,
        IsBank:item.IsBank,
        ModuleID:item.ModuleID,
        key: index,
        size: '15px'
      })
      )
      setClientTableData(dataAll);
      console.log(dataAll);

     
     
    } catch (e) {
      console.log(e)
    }
};
   

    function onChange(value) {
        console.log(`selected ${value}`);
    }
    function handleChange(value) {
        console.log(`selected ${value}`);
    }
    const [form] = Form.useForm()
    const [componentSize, setComponentSize] = useState('small');
    const onFormLayoutChange = ({ size }) => {
        setComponentSize(size);
    };
    const tailLayout = {
        wrapperCol: { offset: 10 },
    };
    const FormItem = Form.Item;

    function onChange(checkedValues) {
        console.log('checked = ', checkedValues);
    }
    const onAddNewClient = async () => {
        props.history.push("/AddClient", menuData)
    };
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
                        <Card title="Client Resgistraion" bordered={false} style={{ width:1500 }} >
                            <Form  form={form}>      
                                <Row gutter={[24, 24]}>
                                    <Col span={6} >
                                        <Form.Item
                                        label="Domain Type"
                                        name="domainid"
                                    >
                                       <Select defaultValue="select"  onChange={onChange}>
                                       <Option value="select">--select--</Option> 
                                           {domaindata}
                                        </Select>
                                    </Form.Item>
                                    </Col>
                                    <Col span={6}>
                                    <Form.Item
                                            label="Module Type"
                                            name="moduleid"
                                        >
                                            <Select defaultValue="--select--"  onChange={onChange}>
                                            <Option value="select">--select--</Option>
                                           {moduledata}
                                        </Select>
                                    </Form.Item>
                                    </Col>
                                    <Col span={6}>
                                    <Form.Item
                                            label="Client Name"
                                            name="clientid"
                                    >
                                            <Select defaultValue="--select--"  onChange={onChange}>
                                            <Option value="select">--select--</Option>
                                           {clientdata}
                                        </Select>
                                    </Form.Item>                                      
                                    </Col>
                                    <Col span={6}>
                                    <Form.Item>
                                            <Button type="primary" onClick={onClientSearch}>
                                                Search
                                            </Button>
                                            <Button style={{ margin: '0 8px' }} onClick={onAddNewClient} >
                                                Add New
                                                {/* <NavLink to={"/addUser"}>Add User</NavLink> */}
                                            </Button>                                            
                                        </Form.Item>   
                                    </Col>
                                </Row>
                            </Form>
                        </Card>
                        <Card>
                        {
                            loader ? (
                            <Table columns={columns} size={"large"}
                                dataSource={clientGetData} scroll={{ x: 240 }} bordered />
                            ):("") 
                        }
                        </Card>
                    </Content>
                </Layout>
            </Layout>
        </Layout>
    );
};
export default ClientRegistration;