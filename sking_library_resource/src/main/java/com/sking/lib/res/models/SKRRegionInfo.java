package com.sking.lib.res.models;

/**
 * 滑动选择器model,处理时可以直接继承此类，再添加服务返回其它参数即可
 *
 * **/
public class SKRRegionInfo {

	private int id;// id
	private int type;// 类型
	private int parent;// 父级
	private String name;// 名称

	public SKRRegionInfo() {
	}

	public SKRRegionInfo(int id, int parent, String name) {
		super();
		this.id = id;
		this.parent = parent;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RegionInfo [name=" + name + "]";
	}

	//这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
	public String getPickerViewText() {
		//这里还可以判断文字超长截断再提供显示
		return name;
	}


}
