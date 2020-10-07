import React, { useContext } from 'react';
import { withRouter } from "react-router-dom";
import { Layout, Menu } from 'antd';
import MenuContext from '../utils/MenuContext';
import {
    DesktopOutlined,   
    FileOutlined,
} from '@ant-design/icons';
import SubMenu from 'antd/lib/menu/SubMenu';
import axios, { axiosGet } from '../utils/axios';

const MenuSideBar = props => {
    //console.log(props)

// const menuContext = useContext(MenuContext)


    const _renderSubMenuItem = (item) => {
        // <span>{(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()}</span>
        const menuData = props.menuData;
        // console.log(menuData,menuContext)
        // if(!menuData) return;
        const subMenus = JSON.parse(menuData.subMenu).filter(menu => menu.ParentMenuID == item.MenuID);
        // console.log("menuData",menuData)
        // console.log("subMenus",subMenus)
        if (subMenus && subMenus.length > 0) {
            return <SubMenu key={item.MenuID} title={(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()} icon={<FileOutlined />}>
                {subMenus.map(item => <Menu.Item key={item.MenuID,item.FilePath}>

                    <DesktopOutlined />
                    {/* <NavLink to={item.FilePath} onClick={onSubMenu}> <span>{(item.MenuName)}</span></NavLink> */}
                    <span>{item.MenuName}</span>
  
                   
                </Menu.Item>)}
            </SubMenu>
        } else {
            return (<Menu.Item key={item.MenuID}>
                <DesktopOutlined />
                <span>{(item.MenuName.split("&nbsp;&nbsp;")[1]).trim()}</span>
            </Menu.Item>)
        }

    }
    const renderMenuItems = () => {

        const menudata = props.menuData;
        if (menudata && menudata.menu) {
            // debugger;
            const menu = JSON.parse(menudata.menu);
          //  console.log(menu);
            const data= menu.map((item, i) => _renderSubMenuItem(item) )
//console.log("Menus",data);
return data
        }else{
            return "invalid"
        }

    }

    const onMemuSelect = (params) => {
        console.log(params)
        props.history.push(params.key,props.menuData)
    }

    const onSubMenu =async()=>
    {
        try
        {

            const response = await axios.get(`GetRoleDetails`);
            console.log(response.data)
            props.history.push("/RoleDetails",response.data)

          
        }catch(e){
           console.log(e);
        }
        
    }
    
        // console.log("MenuItems",null?renderMenuItems():"Not found",menuContext)
    return(

    
   
        <Layout.Sider collapsible style={{height:"100vh"}}>
            <Menu theme="dark" onSelect={onMemuSelect} mode={"inline"}>
                {renderMenuItems()}
            </Menu>
        </Layout.Sider>
        ); 
}

export default withRouter(MenuSideBar)