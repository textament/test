/**
 *
 */
package com.shanshengyuan.client.model.dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shanshengyuan.client.BaseRequest;
import com.shanshengyuan.client.DeviceKey;
import com.shanshengyuan.client.utils.StringUtils;

/**
 * @author Administrator
 *
 */
public class DishListRequest extends BaseRequest {

    private String channel; //终端类型 0.app 1.微信
    private String appId;//终端标识：手机IMEI号与微信的openId
    private String timestamp; //终端本地时间戳
    private String version; //终端版本号
    private String apiVersion;//接口版本号
    private String key;//密匙
    private int pageSize = 10;
    private int page = 1;
    private Integer dishTypeId;
    private String searchDishName;



    public String getSearchDishName() {
        return searchDishName;
    }

    public void setSearchDishName(String searchDishName) {
        this.searchDishName = searchDishName;
    }

    public void next() {
        setPage(getPage() + 1);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }





    public Integer getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Integer dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public void setDk() {

        List<String> dkList;

        DeviceKey dk;

        dk = new DeviceKey();
        dkList = new ArrayList<String>();
        dkList.add("channel:" + dk.getChannel());
        dkList.add("appId:" + dk.getAppId());
        dkList.add("timestamp:" + dk.getTimestamp());
        dkList.add("version:" + dk.getVersion());
        dkList.add("apiVersion:" + dk.getApiVersion());
        dkList.add("page:" + getPage());
        dkList.add("pageSize:" + getPageSize());
        if(dishTypeId!=null)
            dkList.add("dishTypeId:" + getDishTypeId());

        if(!StringUtils.isEmpty(getHasProcessDialog())){
            dkList.add("hasProcessDialog:" + getHasProcessDialog());
        }

        if(!StringUtils.isEmpty(searchDishName)){
            dkList.add("searchDishName:" + getSearchDishName());
        }

        String s = "";
        List<String> list = dkList;
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            s += list.get(i) + "@";
        }
        String s1 = s.substring(0, s.lastIndexOf("@"));
        setChannel(dk.getChannel());
        setAppId(dk.getAppId());
        setTimestamp(dk.getTimestamp());
        setVersion(dk.getVersion());
        setApiVersion(dk.getApiVersion());
        setKey(StringUtils.MD5(s1));

    }
}
