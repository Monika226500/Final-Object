package com.zhumeng.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author weikun
 * 页面参数对象
 */
@Data
public class AIFaceBean implements Serializable { //前端给后端的数据
	private String imgdata;
	private String error_code;
	private String error_msg;
	private float score;

}
