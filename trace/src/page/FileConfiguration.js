import React, { useContext,useState, useEffect,useRef } from 'react';
import 'antd/dist/antd.css';
import axios from '../utils/axios';
import MenuSideBar from './menuSideBar';
import {
  Form,
  Input,
  Button,
  Select,
  Card,
  Row,
  Col,
  Layout,
  Avatar,
  Table,
  Space,
  Popconfirm,
} from 'antd';
import Title from 'antd/lib/typography/Title';
import $ from 'jquery';

const { Header,Content } = Layout;
const { Option } = Select;

const FileConfiguration = props => {
  // console.log(props)
  const [ClientData, setClientData] = useState([])
  const [logData, setLogData] = useState([])
  const [P_VENDORTYPE, setLogType] = useState([])
  const [modeData, setModeData] = useState([])
  const [P_CLIENTID, setClientID] = useState([])
  const [vendorData, setVendorData] = useState([])
  const [ChannelData, setChannelData] = useState([])
  const [P_CHANNELID, setChannelID] = useState([])
  const [P_MODEID, setModelID] = useState([])
  const [P_FILEType, setFileTypeID] = useState([])
  const [fileExt, setFileExt] = useState([])
  const [P_VENDORID, setVendorID] = useState([])
  const [cname, setClientName] = useState([])
  const [fid, setFormatID] = useState([])
  const [ConfigTableData, setConfigTableData] = useState([])
  const [inputFilePre, setInputFilePre] = useState([])
  const [inputCutOff, setInputCutOff] = useState([])
  const [xmltable, setXmlTable] = useState([])
  const [tblfieldNames, setFieldNames] = useState([])
  const [upFieldName, setFieldName] = useState([])
  const [upFieldLen, setFieldLength] = useState([])
  const [upFieldStartPos, setStartPosition] = useState([])
  const [updatedTableData, setUpdatedTableData] = useState([])
  const [septype, setSepType] = useState([])
  const [valueTime, setValue] = useState([])
  
  const [fileloader, setFilePlaintextLoader] = useState(false)
  const [filetype, setFileLoader] = useState(false)
  const [channelloader, setChannelLoader] = useState(false)
  const [separatorloader, setSeperator] = useState(false)
  const [loader, setLoader] = useState(false)
  const [configLoader, setConfigTable] = useState(false)
  const [clientInfoCard, setClientInfoCard] = useState(false)
  const [npciTable, setNPCITable] = useState(false)
  const [cbsTable, setCBSTable] = useState(false)
  const [switchTable,setSwitchTable]=useState(false)
  const [ejtable,setEJTable]=useState(false)
  const [cbsTabletxt, setCBSTableTxt] = useState(false)

  const [editingKey, setEditingKey] = useState('');
  const isEditing = record => record.key === editingKey;
//----------------------------------------------TABLE TEST-----------------------------------------------------------------
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

  const handleSave =  (row) => {
    // const newData = [...this.state.dataSource];
    // const index = newData.findIndex((item) => row.key === item.key);
    // const item = newData[index];
    // newData.splice(index, 1, { ...item, ...row });
   

    //const row = await form.validateFields();
        const newData = [...xmltable];
        const index = newData.findIndex((item) => row.key === item.key);
  
        if (index > -1) {
          const item = newData[index];
          newData.splice(index, 1, { ...item, ...row });
          setXmlTable(newData);
          setEditingKey('');
        } else {
          newData.push(row);
          setXmlTable(newData);
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

const columns = [{
  title: 'Field Name',
  dataIndex: 'FieldName',
  render: text => <a>{text}</a>,
},
{
  title: 'Start Position',
  dataIndex: 'StartPosition',
  editable: true,
  render: (text, record) => (
    <Space size="middle">
      {record.StartPosition}
    </Space>
  ),
},
{
  title: 'Field Length',
  dataIndex: 'FieldLength',
  key: 'FieldLength',
  editable: true,
  render: (text, record) => (
    <Space size="middle">
      {record.FieldLength}

    </Space>
  ),
},

];
    const columnsTest = columns.map((col) => {
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

//-------------------------------------------------------------------------------------------------------------------------
//edit table function for npci table

  //EDIT CODE FOR EJ TABLE
  const EditableCellEJ = ({
    editing,
    dataIndex,
    title,
    inputType,
    record,
    index,
    children,
    ...restProps
  }) => {
    const inputNode = dataIndex === 'startPositionEJ' ? <Input  /> : <Input  />;
    return (
      <td {...restProps}>
        {editing ? (
          <Form.Item
            name={dataIndex}
            style={{
              margin: 0,
            }}
            rules={[
              {
                required: true,
                message: `Please Input ${title}!`,
              },
            ]}
          >
            {inputNode}
          </Form.Item>
        ) : (
            children
          )}
      </td>
    );
  };
  const editEJ = (record) => {
    form.setFieldsValue({
      Position: '',

      ...record,
    });
    setEditingKey(record.key);
  };


//EDIT CODE FOR Switch TABLE
const EditableCellSwitch = ({
  editing,
  dataIndex,
  title,
  inputType,
  record,
  index,
  children,
  ...restProps
}) => {
  const inputNode = dataIndex === 'StartPositionSwitch' ? <Input /> : <Input />;
  return (
    <td {...restProps}>
      {editing ? (
        <Form.Item
          name={dataIndex}
          style={{
            margin: 0,
          }}
          rules={[
            {
              required: true,
              message: `Please Input ${title}!`,
            },
          ]}
        >
          {inputNode}
        </Form.Item>
      ) : (
          children
        )}
    </td>
  );
};
const editSwitch = (record) => {
  form.setFieldsValue({
    StartPositionSwitch: '',
    ...record,
  });
  setEditingKey(record.key);
};


  const cancel = () => {
    setEditingKey('');
  };

  const columnsConfigFormat = [{
    title: 'ClientName',
    dataIndex: 'ClientName',
    key: 'ClientName',
    render: text => <a>{text}</a>,
  },
  {
    title: 'LogType',
    dataIndex: 'LogType',
    key: 'LogType',
    /*render: (roleID,record) => (
      <Space size="middle">
        <a onClick={() => { onAccess(record) }}><UserSwitchOutlined /></a>
        {console.log(record)}
      </Space>
    ),*/
  },
  {
    title: 'Channel',
    dataIndex: 'Channel',
    key: 'Channel',
    /*render: (text, record) => (
      <Space size="middle">
        <a><EditOutlined /></a>
      </Space>
    ),*/
  },
  {
    title: 'FilePrefix',
    dataIndex: 'FilePrefix',
    key: 'FilePrefix',

  },
  {
    title: 'Mode',
    dataIndex: 'Mode',
    key: 'Mode',

  },
  {
    title: 'VendorName',
    dataIndex: 'VendorName',
    key: 'VendorName',

  },
  {
    title: 'FileExtention',
    dataIndex: 'FileExtention',
    key: 'FileExtention',

  }];

  
  const columnsCBS = [{
    title: 'Field Name',
    dataIndex: 'FieldNameCBS',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Position',
    dataIndex: 'StartPositionCBS',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.StartPositionCBS}
      </Space>
    ),
  },
  
  ];


  const columnsCBSTxt = [{
    title: 'Field Name',
    dataIndex: 'FieldNameCBS',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Position',
    dataIndex: 'StartPositionCBS',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.StartPositionCBS}
      </Space>
    ),
  },
  {
    title: 'Field Length',
    dataIndex: 'LengthNodeCBS',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.LengthNodeCBS}
      </Space>
    ),
  },
  
  ];
  const mergedColumnsCBSTxt = columnsCBSTxt.map((col) => {
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
  const mergedColumnsCBS = columnsCBS.map((col) => {
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



  const columnsEJ = [{
    title: 'Field Name',
    dataIndex: 'FieldNameEJ',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Position',
    dataIndex: 'StartPositionEJ',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.StartPositionEJ}
      </Space>
    ),
  },
  ];
  const mergedColumnsEJ = columnsEJ.map((col) => {
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

  const columnsSwitch = [{
    title: 'Field Name',
    dataIndex: 'FieldNameSwitch',
    render: text => <a>{text}</a>,
  },
  {
    title: 'Position',
    dataIndex: 'StartPositionSwitch',
    editable: true,
    render: (text, record) => (
      <Space size="middle">
        {record.StartPositionSwitch}
      </Space>
    ),
  },
  
  ];
  const mergedColumnsSwitch = columnsSwitch.map((col) => {
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

  useEffect(() => {
    onDisplayClientNameList();
    onDisplayChannel();
    //onTestXml();
  }, [])


  const format = 'HH:mm';

  const onDisplayClientNameList = async () => {
    try {
      const clientNameResponse = await axios.get(`clientName`);
      //console.log(clientNameResponse.data)
      setLoader(false);

      const clientNameN = clientNameResponse.data;
      // console.log(clientNameN);
      const clientNameList = clientNameN.map((item, index) =>
        <Option value={item.id} key={index}>{item.clientNameList}
        </Option>
      )
      setClientData(clientNameList);
    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayLogFileList = async (value) => {
    try {
      const logNameResponse = await axios.get(`getLogList/${value}`);
      // console.log(logNameResponse.data)
      setLoader(false);

      const logNameN = logNameResponse.data;
      //console.log(logNameN)
      const clientNameList = logNameN.map((item, index) =>
        <Option value={item.ID} key={index}>{item.LogType}
        </Option>
      )
      setLogData(clientNameList);

    } catch (e) {
      console.log(e)
    }
  };
  const onDisplayChannelList = async (P_CLIENTID) => {
    try {
      //console.log(P_CLIENTID)
      const channelResponse = await axios.get(`getChannelID`);
      //  console.log(channelResponse.data)
      setLoader(false);

      const channelN = channelResponse.data;
      //console.log(channelN);

      const listChannel = channelN.map((item, index) => <Option value={item.roleID} key={index} label={item.channeltype}>{item.channeltype}</Option>)
      setChannelData(listChannel);


    } catch (e) {
      console.log(e)
    }
  };


  const onDisplayModeType = async (P_CLIENTID, value) => {
    try {
      const modeTypeResponse = await axios.get(`getModeType/${P_CLIENTID}/${value}`);
      // console.log(modeTypeResponse.data)
      //setLoader(false);
      const modeTypeN = modeTypeResponse.data;
      //console.log(modeTypeN);
      const listmodeType = modeTypeN.map((item, index) => <Option value={item.modeid} key={index} label={item.modename}>{item.modename}</Option>)
      setModeData(listmodeType);
    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayVendor = async (checkedValues) => {
    try {
      const vendorResponse = await axios.get(`getVendorDetailsByType/${checkedValues}`);
      // console.log(vendorResponse.data)
      setLoader(false);

      const vendorN = vendorResponse.data;

      const listVendor = vendorN.map((item, index) => <Option value={item.VendorID} key={index}>{item.VendorName}</Option>)
      setVendorData(listVendor);

    } catch (e) {
      console.log(e)
    }
  };
  const onDisplayChannel = async () => {
    try {
      const channelResponse = await axios.get(`getChannelID`);
      // console.log(channelResponse.data)
      setLoader(false);

      const channelN = channelResponse.data;
      //console.log(channelN);

      const listChannel = channelN.map((item, index) => <Option value={item.roleID} key={index} label={item.channeltype}>{item.channeltype}</Option>)
      setChannelData(listChannel);

    } catch (e) {
      console.log(e)
    }
  };

  const onDisplayConfigTable = async (value) => {
    try {
      const configTableResponse = await axios.get(`getfileformatclient/${value}`);
      console.log(configTableResponse.data)
      setLoader(false);

      const ConfigTableN = configTableResponse.data;
      console.log(ConfigTableN);

      const listConfigTable = ConfigTableN.map((item, index) => ({
        ClientName: item.clientName,
        LogType: item.vendorType,
        Channel: item.channelName,
        FilePrefix: item.filePrefix,
        Mode: item.transactionMode,
        VendorName: item.vendorName,
        FileExtention: item.fileExtension,
        key: index
      })
      )

      setConfigTableData(listConfigTable);
    } catch (e) {
      console.log(e)
    }
  };

  const menuData = props.location.state;
  //console.log(menuData);

  const onFilePre = event => {
    setInputFilePre(event.target.value);
  }

  const onFileCuttoff = event => {
    setValue(event.target.value);
  }


  // const onCutOff = time => {
  //   setValue(time);

  // };

  // const onXmlRead = event => {
  //   alert(event.target.time);
  //   //setInputXMLRead(event.target.time);
  // }


  function onChangeClientName(value) {
    console.log(`selected ${value}`);
    onDisplayLogFileList(value);
    onDisplayConfigTable(value);
    setClientID(value);
    setConfigTable(true);
  }
  function onChangeLog(checkedValues) {
    console.log('checked = ', checkedValues);
    setLogType(checkedValues)
    onDisplayVendor(checkedValues);
    onDisplayChannelList(P_CLIENTID);
    setChannelLoader(true)

  }
  function onChangeChannel(value) {
    console.log(`selected ${value}`);
    setChannelID(value)
    onDisplayModeType(P_CLIENTID, value)
  }

  function onChangeFileType(checkedValues) {
    console.log('checked = ', checkedValues);
    setFileTypeID(checkedValues)

    if (checkedValues == 'spreadsheet') {
      setFileTypeID(checkedValues)
      setFileLoader(true);
      setFilePlaintextLoader(false);
      setSeperator(false)
    }
    else if (checkedValues == 'plaintext') {
      setFilePlaintextLoader(true);
      setFileLoader(false);
      setSeperator(false)
    }
    if (checkedValues == 'plaintextwithSeparator') {
      setFilePlaintextLoader(false);
      setFileLoader(false);
      setSeperator(true)
    }
  }

  function onChangeMode(checkedValues) {
    console.log('checked = ', checkedValues);
    setModelID(checkedValues)
  }



  function onChangeVendor(checkedValues) {
    console.log('checked = ', checkedValues);
    setVendorID(checkedValues);
    getFileFormatHistory(P_VENDORTYPE, P_CLIENTID, P_CHANNELID, P_MODEID, checkedValues);
    setClientInfoCard(true);
  }
  // function onChange(checkedValues) {
  //   console.log('checked = ', checkedValues);
  // }
   function onChangeExt(checkedValues) {
    console.log('checked = ', checkedValues);
    setFileExt(checkedValues)
  }

  function onChangeSepType(value) {
    console.log(`selected ${value}`);
    setSepType(value);
  }

  const [form] = Form.useForm()

  const getFileFormatHistory = async (P_VENDORTYPE, P_CLIENTID, P_CHANNELID, P_MODEID, P_VENDORID) => {
    try {
      //console.log(inputFilePre);
      const values = form.getFieldsValue();
      console.log(values)
      console.log(P_VENDORID)
      const response = await axios.get(`getFileFormatHistory/${P_VENDORTYPE}/${P_CLIENTID}/${P_CHANNELID}/${P_MODEID}/${P_VENDORID}/${values.P_FILEType}/${values.P_FILEEXT}/${values.P_FILEPREFIX}`);
       console.log(response.data)
       const formatHistoryResponse = response.data;
      const logfile=values.P_FILEType;
      alert(logfile);
      if (P_VENDORTYPE == "NETWORK"){
      //if(logfile=="plaintext"){
        setNPCITable(true);
        setCBSTable(false);
        setSwitchTable(false);
        setEJTable(false);
        //const npxixmldata = JSON.parse(JSON.stringify(formatHistoryResponse.NETWORK2));
        //  const clientNameSet = formatHistoryResponse.map(item =>  item.ClientName );
        //  const FormatIDSet=formatHistoryResponse.map(item=>item.FormatID);
        //  const FieldNames=formatHistoryResponse.map(item=>item.FieldName);
        //  setClientName(clientNameSet);
        //  setFormatID(FormatIDSet);
        //  setFieldNames(FieldNames);
        //  alert(tblfieldNames);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldName: item.NodeName,
          StartPosition: item.startPosNodeValueNode,
          FieldLength: item.LengthNodeValueNode,
          key: index,
        })
        )
        setXmlTable(listXml);
      }
      if(P_VENDORTYPE == "NETWORK" && P_MODEID=="0"){
        //alert("inside NTSl")
        setCBSTable(true);
        setNPCITable(false);
        setSwitchTable(false);
        setEJTable(false);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldNameCBS: item.NodeName,
          StartPositionCBS: item.indexPosition,
          key: index,
        })
        )
        setXmlTable(listXml);
      }
      

      if(P_VENDORTYPE == "CBS"){
        setCBSTable(true);
        setNPCITable(false);
        setSwitchTable(false);
        setEJTable(false);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldNameCBS: item.NodeName,
          StartPositionCBS: item.indexPosition,
          key: index,
        })
        )
        setXmlTable(listXml);
      }
      var ext=values.P_FILEEXT
      alert('ext file',ext);
      if(P_VENDORTYPE == "CBS" && ext==".txt"){
        setCBSTableTxt(true);
        setCBSTable(false);
      //  setCBSTable(true);
        setNPCITable(false);
        setSwitchTable(false);
        setEJTable(false);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldNameCBS: item.NodeName,
          StartPositionCBS: item.startPosNodeValueNode,
          LengthNodeCBS:item.LengthNodeValueNode,
          key: index,
        })
        )
        setXmlTable(listXml);
      }
      
      if(P_VENDORTYPE=="EJ"){
        setEJTable(true);
        setCBSTable(false);
        setNPCITable(false);
        setSwitchTable(false);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldNameEJ: item.NodeName,
          StartPositionEJ: item.indexPosition,
          key: index,
        })
        )
        setXmlTable(listXml);
      }

      if(P_VENDORTYPE=="SWITCH"){
        setSwitchTable(true);
        setCBSTable(false);
        setNPCITable(false);
        setEJTable(false);
        const listXml = formatHistoryResponse.map((item, index) => ({
          FieldNameSwitch: item.NodeName,
          StartPositionSwitch: item.indexPosition,
          key: index,
        })
        )
        setXmlTable(listXml);
      }

      // console.log("test check",listXml[1]);
      // var xmlTblField=[];
      // alert(listXml.length);
      // for(var i=1;i<listXml.length;i++)
      // {
      //   xmlTblField.push(listXml[i]);
      // }
      // console.log(xmlTblField);

      // setXmlTable(listXml);

      //  if(JSON.stringify(response.data) === 'Save')
      //  {
      //    alert("user added successfully");
      //  }
      //  else{9
      //    alert("already exist");
      //  }
      //props.history.push("/AddUser",response.data)
      
    } catch (e) {
      console.log(e)
    }
  }


  function searchText(myxmlString) {
    //alert(myxmlString);
    //  const response = await axios.post(`getinsertfileformat/${P_CLIENTID}/${P_VENDORTYPE}/${P_CHANNELID}/${P_MODEID}/${values.P_FILEEXT}/${P_VENDORID}`,formDataFilePre,formDataCutOff);
   if(P_VENDORTYPE=='NETWORK'){
    const filePrefix = inputFilePre;
    const cutOff = valueTime;

    // var values=form.getFieldsValue();
    // console.log(values.P_SEPARATORTYPE);
    // alert("value in sep",septype);
    // var styp=(septype!='undefined') ? (septype):(0);
    // alert("sept type",styp);
    var xmlcls = {
      "myXmlData": myxmlString,
      "clientID": P_CLIENTID,
      "vendorType": P_VENDORTYPE,
      "channelID": P_CHANNELID,
      "modeID": P_MODEID,
      "fileExt": fileExt,
      "vendorID": P_VENDORID,
      "filePre": filePrefix,
      "cutOffTime": valueTime,
      // "stype":styp,
    }

   }

   if(P_VENDORTYPE=='CBS'){
    // alert("in ajax cbc");
     const filePrefix = inputFilePre;
     const cutOff = valueTime;
     var values=form.getFieldsValue();
     console.log(values.P_SEPARATORTYPE);
    // alert("value in sep",septype);
     var styp=(septype!='undefined') ? (septype):(0);
     //alert("sept type",styp);
   
     var xmlcls = {
      "myXmlData": myxmlString,
      "clientID": P_CLIENTID,
      "vendorType": P_VENDORTYPE,
      "channelID": P_CHANNELID,
      "modeID": P_MODEID,
      "fileExt": fileExt,
      "vendorID": P_VENDORID,
      "filePre": filePrefix,
      "cutOffTime": valueTime,
      "stype":styp,
    }

   }
  
   if(P_VENDORTYPE=='SWITCH'){
    //alert("in ajax switch");
    const filePrefix = inputFilePre;
    const cutOff = valueTime;

    var xmlcls = {
     "myXmlData": myxmlString,
     "clientID": P_CLIENTID,
     "vendorType": P_VENDORTYPE,
     "channelID": P_CHANNELID,
     "modeID": P_MODEID,
     "fileExt": fileExt,
     "vendorID": P_VENDORID,
     "filePre": filePrefix,
     "cutOffTime": valueTime,
     // "stype":styp,
   }

  }
 
  if(P_VENDORTYPE=='EJ'){
    //alert("in ajax EJ");
    const filePrefix = inputFilePre;
    const cutOff = valueTime;

    var xmlcls = {
     "myXmlData": myxmlString,
     "clientID": P_CLIENTID,
     "vendorType": P_VENDORTYPE,
     "channelID": P_CHANNELID,
     "modeID": P_MODEID,
     "fileExt": fileExt,
     "vendorID": P_VENDORID,
     "filePre": filePrefix,
     "cutOffTime": valueTime,
     // "stype":styp,
   }

  }
 
    $.ajax({
      type: "POST",
      contentType: 'application/json; charset=utf-8',
      dataType: 'json',
      url: "http://192.168.1.130:8080/Admin/api/getxmlfileformat",
      //url: "http://localhost:8080/Admin/api/getxmlfileformat",
      data: JSON.stringify(xmlcls), // Note it is important
      success: function (result) {
       console.log(result);
      }
    });
  }

  const onFileConfig = async () => {
    try {
      console.log(xmltable);

   if(P_VENDORTYPE=="NETWORK" && P_MODEID=="0")
   {
   // alert("cbs xml");
    var nodeNameTest = xmltable.map(item => item.FieldNameCBS);
    var filedNames = [];
    for (var i in nodeNameTest)
    filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
   
    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPositionCBS);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
    posValue.push(startPosNodeValueNodeTest[i]);
   // alert(startPosNodeValueNodeTest);

   
    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i],posValue[i]);
     // var item2 = root.ele('StartPosition', posValue[i]);
    }
   }
if(P_VENDORTYPE=="NETWORK" && P_MODEID!=0)
      {
       // alert("network xml");
        var nodeNameTest = xmltable.map(item => item.FieldName);
        var filedNames = [];
        for (var i in nodeNameTest)
        filedNames.push(nodeNameTest[i]);
        console.log(filedNames);
        // alert("filedNames len"+filedNames.length);
        // alert("filedNames[1]"+filedNames[1]);
  
        var startPosNodeValueNodeTest = xmltable.map(item => item.StartPosition);
        var posValue = [];
        for (var i in startPosNodeValueNodeTest)
          posValue.push(startPosNodeValueNodeTest[i]);
        //alert(startPosNodeValueNodeTest);
  
        var LengthNodeValueNode = xmltable.map(item => item.FieldLength);
        var lenValue = [];
        for (var i in LengthNodeValueNode)
          lenValue.push(LengthNodeValueNode[i]);
        //alert(LengthNodeValueNode);
  
        var builder = require('xmlbuilder');
  
        var root = builder.create('FileFormat');
        for (var i = 0; i < filedNames.length; i++) {
          //while(nodeNameTest.length!=0){
          var item = root.ele(filedNames[i]);
          var item2 = item.ele('StartPosition', posValue[i]);
          var item3 = item.ele('Length', lenValue[i]);
        }
        // item.att('x');
        // item.att('y');
        //}
   }

   

   if(P_VENDORTYPE=="CBS"  && fileExt!=".txt")
   {
   // alert("cbs xml");
    var nodeNameTest = xmltable.map(item => item.FieldNameCBS);
    var filedNames = [];
    for (var i in nodeNameTest)
    filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
   
    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPositionCBS);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
    posValue.push(startPosNodeValueNodeTest[i]);
   // alert(startPosNodeValueNodeTest);

   
    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i],posValue[i]);
     // var item2 = root.ele('StartPosition', posValue[i]);
    }
   }

   if(P_VENDORTYPE=="CBS" && fileExt==".txt")
   {
    alert("cbs text xml");
    var nodeNameTest = xmltable.map(item => item.FieldNameCBS);
    var filedNames = [];
    for (var i in nodeNameTest)
    filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
   
    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPositionCBS);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
    posValue.push(startPosNodeValueNodeTest[i]);
   // alert(startPosNodeValueNodeTest);

    var lennode=xmltable.map(item=>item.LengthNodeCBS);
    var lenValue=[];
    for(var i in lennode)
    lenValue.push(lennode[i]);

    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i]);
      var item2 = item.ele('StartPosition', posValue[i]);
      var item3 = item.ele('Length', lenValue[i]);
    }
   }

   if(P_VENDORTYPE=="EJ")
   {
   // alert("cbs xml");
    var nodeNameTest = xmltable.map(item => item.FieldNameEJ);
    var filedNames = [];
    for (var i in nodeNameTest)
    filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
   
    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPositionEJ);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
    posValue.push(startPosNodeValueNodeTest[i]);
    //alert(startPosNodeValueNodeTest);

    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i],posValue[i]);
     // var item2 = root.ele('StartPosition', posValue[i]);
    }
   }

   if(P_VENDORTYPE=="SWITCH")
   {
    //alert("cbs xml");
    var nodeNameTest = xmltable.map(item => item.FieldNameSwitch);
    var filedNames = [];
    for (var i in nodeNameTest)
    filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
   
    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPositionSwitch);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
    posValue.push(startPosNodeValueNodeTest[i]);
   // alert(startPosNodeValueNodeTest);

   
    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i],posValue[i]);
     // var item2 = root.ele('StartPosition', posValue[i]);
    }
    

   }
   
      console.log(root);

      var xml = root.end({ pretty: false });
      var myxmlString = root.toString();
      console.log(myxmlString);
      searchText(myxmlString);

      const filePrefix = inputFilePre;
      const formDataFilePre = new FormData();
      formDataFilePre.append('P_FILEPREFIX', filePrefix);
      console.log(formDataFilePre);

      const cutOff = inputCutOff;
      const formDataCutOff = new FormData();
      formDataCutOff.append('P_CUTOFFTIME', cutOff);

      const abc = fileExt.split('.');
      const P_FILEEXT = abc[0];
      // console.log(P_FILEEXT[1]);
      // var P_FILEXML=xml;
      //  const xyz=putXMl.value;
      //  const formXmlData=new FormData();
      //  formXmlData.append('P_FILEXML',xyz);
      // console.log(formXmlData);

      // console.log(formXmlData);
      const validateFields = await form.validateFields();
      // const values = form.getFieldsValue();
      // console.log(values);
      // const response = await axios.post(`getinsertfileformat/${P_CLIENTID}/${P_VENDORTYPE}/${P_CHANNELID}/${P_MODEID}/${values.P_FILEEXT}/${P_VENDORID}`,formDataFilePre,formDataCutOff);
      // console.log(response.data);
      /*
     if(JSON.stringify(response.data) === 'Save')
     {
       alert("user added successfully");
     }
     else{
       alert("already exist");
     }
      //props.history.push("/AddUser",response.data)
*/
    } catch (e) {
      console.log(e)
    }
  }

  const save = async (key) => {
    try {
      const row = await form.validateFields();
      const newData = [...xmltable];
      const index = newData.findIndex((item) => key === item.key);

      if (index > -1) {
        const item = newData[index];
        newData.splice(index, 1, { ...item, ...row });
        setXmlTable(newData);
        setEditingKey('');
      } else {
        newData.push(row);
        setXmlTable(newData);
        setEditingKey('');
      }
      console.log("updated data", newData);
      setUpdatedTableData(newData);
      //writeXml(newData);

      //  props.history.push("/FileConfiguration#", menuData)
      //window.location.reload(false);
      return false;
    } catch (errInfo) {
      console.log('Validate Failed:', errInfo);
    }
  };

  const writeXml = async () => {
    // var xmlDataTbl=updatedTableData;
    //console.log(xmlDataTbl);
    console.log(xmltable);
    var nodeNameTest = xmltable.map(item => item.FieldName);
    var filedNames = [];
    for (var i in nodeNameTest)
      filedNames.push(nodeNameTest[i]);
    console.log(filedNames);
    // alert("filedNames len"+filedNames.length);
    // alert("filedNames[1]"+filedNames[1]);

    var startPosNodeValueNodeTest = xmltable.map(item => item.StartPosition);
    var posValue = [];
    for (var i in startPosNodeValueNodeTest)
      posValue.push(startPosNodeValueNodeTest[i]);
    //alert(startPosNodeValueNodeTest);

    var LengthNodeValueNode = xmltable.map(item => item.FieldLength);
    var lenValue = [];
    for (var i in LengthNodeValueNode)
      lenValue.push(LengthNodeValueNode[i]);
    //alert(LengthNodeValueNode);

    var builder = require('xmlbuilder');

    var root = builder.create('FileFormat');
    for (var i = 0; i < filedNames.length; i++) {
      //while(nodeNameTest.length!=0){
      var item = root.ele(filedNames[i]);
      var item2 = item.ele('StartPosition', posValue[i]);
      var item3 = item.ele('Length', lenValue[i]);
    }
    // item.att('x');
    // item.att('y');
    //}

    // var item=root.ele('xmlN.map(item=>item.NodeName)');
    console.log(root);
    var xml = root.end({ pretty: false });
    //setXmlFileNPCIAcq(xml);
    console.log(xml);

  }

 

  const onReset = () => {
    form.resetFields();
    setConfigTable(false);
    setClientInfoCard(false);
  };


  function onFieldChange(e) {
    console.log(`selected Field ${e.target.value}`);
    //setModeID(value);
    //ongetfieldidentification(value);
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
            <div className="site-card-wrapper">
              <Row gutter={16}>
                <Col span={12}>
                  <Card title="File Configuration" bordered={false} style={{ width: 800 }} >
                    <Form layout={"vertical"} size={"large"} form={form} component={false}>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="Bank Name"
                            name="P_CLIENTID"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                              {ClientData}
                            </Select>

                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Log Type"
                            name="P_VENDORTYPE"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select style={{ width: 150 }} onChange={onChangeLog}>
                              {logData}
                            </Select>
                          </Form.Item>
                        </b></Col>
                        {
                          channelloader ? (
                            <Col span={5}><b>
                              <Form.Item
                                label="Channel Type"
                                name="P_CHANNELID"
                                rules={[{ required: true, message: 'required' }]}>
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeChannel}>
                                  {ChannelData}
                                </Select>
                              </Form.Item>
                            </b></Col>
                          ) : ("")}
                        {
                          channelloader ? (
                            <Col span={5}><b>
                              <Form.Item
                                label="Mode Type"
                                name="P_MODEID"
                                rules={[{ required: true, message: 'required' }]}>
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeMode}>
                                  {modeData}
                                </Select>
                              </Form.Item>
                            </b></Col>
                          ) : ("")
                        }
                      </Row>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="File Type"
                            name="P_FILEType"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeFileType}>
                              <Option value={"spreadsheet"}>spreadsheet</Option>
                              <Option value={"plaintext"}>plaintext</Option>
                              <Option value={"plaintextwithSeparator"}>plaintext with Separator</Option>
                            </Select>
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="File Extention"
                            name="P_FILEEXT"
                            rules={[{ required: true, message: 'required' }]}>
                            {
                              filetype ? (
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>
                                  <Option value={".xls"}>.xls</Option>
                                  <Option value={".csv"}>.csv</Option>
                                </Select>
                              ) : fileloader ? (
                                <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>

                                  <Option value={".txt"}>.txt</Option>
                                  <Option value={".RC"}>.RC</Option>

                                </Select>
                              ) : (
                                    <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeExt}>
                                      <Option value={".txt"}>.txt</Option>
                                      <Option value={".csv"}>.csv</Option>
                                    </Select>
                                  )
                            }
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>

                          <Form.Item
                            label="File Prefix"
                            name="P_FILEPREFIX"
                          //rules={[{ required: true, message: 'Please input your Client name!' }]}
                          >
                            <Input name="P_FILEPREFIX" style={{ width: 150 }} onChange={onFilePre} />
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          {separatorloader ? (
                            <Form.Item
                              label="Separator Type"
                              name="P_SEPARATORTYPE"
                            //rules={[{ required: true, message: 'Please input your Client name!' }]}
                            >
                              <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeSepType}>
                                <Option value={"|"}>|</Option>
                                <Option value={"||"}>||</Option>
                                <Option value={"@"}>@</Option>
                                <Option value={"%"}>%</Option>
                                <Option value={","}>,</Option>
                                <Option value={":"}>:</Option>
                                <Option value={"!"}>!</Option>
                              </Select>
                            </Form.Item>
                          ) : ("")}
                        </b></Col>

                      </Row>
                      <Row gutter={[16, 16]} layout="inline">
                        <Col span={5}><b>
                          <Form.Item
                            label="Vendor"
                            name="P_VENDORID"
                            rules={[{ required: true, message: 'required' }]}>
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeVendor}>
                              {vendorData}
                            </Select>
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Cut Off Time:"
                            name="P_CUTOFFTIME"
                          //rules={[{ required: true, message: 'required' }]}
                          >

                            <Input type="time" name="P_CUTOFFTIME" onChange={onFileCuttoff} />

                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="Start Line No:"
                            name="StartLineNo:"
                          //rules={[{ required: true, message: 'required' }]}
                          >
                            <Input style={{ width: 150 }} />
                          </Form.Item>
                        </b></Col>
                        <Col span={5}><b>
                          <Form.Item
                            label="End Line No:"
                            name="EndLineNo:"
                          //rules={[{ required: true, message: 'required' }]}
                          >
                            <Input style={{ width: 150 }} />
                          </Form.Item>
                        </b></Col>
                      </Row>
                      <Row>
                        <Form.Item>
                          <Button type={"primary"} size={"large"} style={{ width: '100px' }} onClick={onFileConfig} >Save</Button>
                          <Button type={"danger"} size={"large"} style={{ margin: '55px' }, { width: '100px' }} onClick={onReset} >reset</Button>
                        </Form.Item>
                      </Row>
                      {configLoader ? (
                        <Card title={"Configured Format"} bordered={false} >
                          <Table columns={columnsConfigFormat} dataSource={ConfigTableData}
                            scroll={{ y: 540 }} bordered />
                        </Card>
                      ) : ("")}

                    </Form>
                  </Card>

                </Col>
                <Col span={10}>
                  {clientInfoCard ? (
                    <Card bordered={false} style={{ width: 1000 }} style={{ height: "100%" }}>
                      <Form title="" layout={"horizontal"} size={"large"} form={form}>
                        <div>
                          <b>
                            <Form.Item
                              label="Client Name:"
                              name="clientname"
                            >
                              {cname}
                            </Form.Item>
                          </b>
                          <Row gutter={4} layout="inline">
                            <Col flex={1}><b>
                              <Form.Item
                                label="Format No:"
                                name="Format No"
                              // rules={[{ required: true, message: 'Please input your Country Code !' }]}
                              >
                                {fid}
                              </Form.Item>
                            </b></Col>
                            <Col flex={5}><b>
                              <Form.Item
                                label="Format Type:"
                                name="Format Type"
                              >
                                Current
                          </Form.Item>
                            </b></Col>
                          </Row>
                          <Form.Item
                            label="Format Set to Other Client:"
                            name="Format Set to Other Client:"
                          >
                            <Select defaultValue="--select--" style={{ width: 150 }} onChange={onChangeClientName}>
                              {ClientData}
                            </Select>
                          </Form.Item>
                        </div>
                        <div style={{ height: "100%" }} >
                          {npciTable ? (
                            <Table
                              components={{
                                body: {
                                  cell: EditableCell,
                                  row: EditableRow,
                                },
                              }}
                              style={{ width: 800 }}
                              columns={columnsTest} dataSource={xmltable}
                              pagination={false}
                              rowClassName="editable-row"
                              scroll={{ y: 800 }}
                              bordered
                            />

                          ) : cbsTable ? (
                            <Table
                              components={{
                                body: {
                                  cell: EditableCell,
                                  row: EditableRow,
                                },
                              }}
                              style={{ width: 800 }}
                              columns={mergedColumnsCBS} dataSource={xmltable}
                              pagination={false}
                              rowClassName="editable-row"
                              scroll={{ y: 800 }}
                              bordered
                            />
                          ) : switchTable ? (
                            <Table
                              components={{
                                body: {
                                  cell: EditableCell,
                                  row: EditableRow,
                                },
                              }}
                              style={{ width: 800 }}
                              columns={mergedColumnsSwitch} dataSource={xmltable}
                              pagination={false}
                              rowClassName="editable-row"
                              scroll={{ y: 800 }}
                              bordered
                            />
                          )
                          :ejtable ? (
                            <Table
                              components={{
                                body: {
                                  cell: EditableCell,
                                  row: EditableRow,
                                },
                              }}
                              style={{ width: 800 }}
                              columns={mergedColumnsEJ} dataSource={xmltable}
                              pagination={false}
                              rowClassName="editable-row"
                              scroll={{ y: 800 }}
                              bordered
                            />
                          ):cbsTabletxt?(
                            <Table
                              components={{
                                body: {
                                  cell: EditableCell,
                                  row: EditableRow,
                                },
                              }}
                              style={{ width: 800 }}
                              columns={mergedColumnsCBSTxt} dataSource={xmltable}
                              pagination={false}
                              rowClassName="editable-row"
                              scroll={{ y: 800 }}
                              bordered/>
                          ):("")}
                        </div>
                      </Form>
                    </Card>
                  ) : ("")}
                </Col>
              </Row>
            </div>
          </Content>
        </Layout>
      </Layout>
    </Layout >
  );
};
export default FileConfiguration;