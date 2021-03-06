package com.avoscloud.leanchatlib.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.avoscloud.leanchatlib.R;
import com.avoscloud.leanchatlib.utils.Constants;
import com.melink.bqmmsdk.widget.BQMMMessageText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;
import java.util.UUID;

/**
 * Created by wli on 15/9/17.
 */
public class ChatItemTextHolder extends ChatItemHolder {

  /**
   * BQMM集成
   * 增加用于展示表情图片的View
   */
  protected BQMMMessageText contentView;

  public ChatItemTextHolder(Context context, ViewGroup root, boolean isLeft) {
    super(context, root, isLeft);
  }

  @Override
  public void initView() {
    super.initView();
    if (isLeft) {
      conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_left_text_layout, null));
      contentView = (BQMMMessageText) itemView.findViewById(R.id.chat_left_text_tv_content);
    } else {
      conventLayout.addView(View.inflate(getContext(), R.layout.chat_item_right_text_layout, null));
      contentView = (BQMMMessageText) itemView.findViewById(R.id.chat_right_text_tv_content);
    }
    contentView.setStickerSize(getContext().getResources().getDimensionPixelSize(R.dimen.bqmm_sticker_size));
  }

  @Override
  public void bindData(Object o) {
    super.bindData(o);
    AVIMMessage message = (AVIMMessage)o;
    if (message instanceof AVIMTextMessage) {
      AVIMTextMessage textMessage = (AVIMTextMessage) message;
      /**
       * BQMM集成
       * 将消息作为BQMM消息处理
       */
      String msgType = "";
      JSONArray msgData = null;
      String messageID = textMessage.getText();//这里把文字消息的内容作为消息ID
      if (messageID == null) {
        messageID = UUID.randomUUID().toString();
      }
      Map attrs = textMessage.getAttrs();
      try {
        msgType = (String) attrs.get(Constants.TXT_MSGTYPE);
      } catch (NullPointerException | ClassCastException ignored) {
      }
      if (msgType == null) {
        msgType = "";
      }
      try {
        msgData = new JSONArray((String) attrs.get(Constants.MSG_DATA));
      } catch (JSONException | NullPointerException | ClassCastException ignored) {
      }
      if (!TextUtils.isEmpty(msgType) && msgData == null) return;
      contentView.showMessage(messageID, textMessage.getText(), msgType, msgData);
    }
  }
}
