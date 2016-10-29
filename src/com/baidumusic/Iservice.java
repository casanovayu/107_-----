/**
 * @Title: Iservice.java 
 * @Package com.baidumusic 
 * @Description: TODO() 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016年10月18日 下午3:41:40 
 * @version V1.0   
 */
package com.baidumusic;

/**
 * @ClassName: Iservice 
 * @Description: TODO() 
 * @author A18ccms a18ccms_gmail_com 
 * @date 2016年10月18日 下午3:41:40 
 *  
 */
public interface Iservice {

	//想暴露的方法都定义在接口中
	public void callPlayMusic();
	public void callPauseMusic();
	public void callReplayMusic();
	public void callSeekTo(int position);
}
