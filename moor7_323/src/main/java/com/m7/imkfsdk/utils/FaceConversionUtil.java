package com.m7.imkfsdk.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.TextView;

import com.m7.imkfsdk.R;
import com.moor.imkf.model.entity.ChatEmoji;
import com.moor.imkf.utils.MoorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情转换工具类*
 */
public class FaceConversionUtil {

	/** 每一页表情的个数 */
	private int pageSize = 20;

	private static FaceConversionUtil mFaceConversionUtil;

	/** 保存于内存中的表情HashMap */
	private HashMap<String, String> emojiMap = new HashMap<String, String>();

	/** 保存于内存中的表情集合 */
	public List<ChatEmoji> emojis = new ArrayList<ChatEmoji>();

	/** 表情分页的结果集合 */
	public List<List<ChatEmoji>> emojiLists = new ArrayList<List<ChatEmoji>>();

	private FaceConversionUtil() {

	}

	public static FaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new FaceConversionUtil();
		}
		return mFaceConversionUtil;
	}

	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(Context context, String str, TextView textView) {
		SpannableString spannableString = new SpannableString(str);
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
//		String zhengze = "\\[[^\\]]+\\]";
		String zhengze = "\\:[^\\:]+\\:";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0, textView);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 添加表情
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId,
								   String spannableString, TextView textView) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}

		if (imgId != 0) {

			Drawable drawable = context.getResources().getDrawable(imgId);
			// 将该图片替换字符串中规定的位置中
			if(drawable != null) {
				drawable.setBounds(0, 0, (int) (1.3D * textView.getTextSize()), (int) (1.3D * textView.getTextSize()));
				SpannableString spannable = new SpannableString(spannableString);
				spannable.setSpan(new ImageSpan(drawable, 0), 0, spannableString.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				return spannable;
			}

		}
		return new SpannableString(spannableString);
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(Context context,
								SpannableString spannableString, Pattern patten, int start, TextView textView)
			throws Exception {

		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			String value = emojiMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable",
					context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			if (resId != 0) {

				Drawable drawable = null;

				if(resId != 0) {
					drawable = context.getResources().getDrawable(resId);
				}

				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length();
				// 将该图片替换字符串中规定的位置中
				if(drawable != null) {
					drawable.setBounds(0, 0, (int) (1.3D * textView.getTextSize()), (int) (1.3D * textView.getTextSize()));
					spannableString.setSpan(new ImageSpan(drawable, 0), matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end, textView);
				}
				break;
			}
		}
	}

	public void getFileText(Context context) {
		ParseData(MoorUtils.getEmojiFile(context), context);
		
	}

	/**
	 * 解析字符
	 * 
	 * @param data
	 */
	private void ParseData(List<String> data, Context context) {
		
		if (data == null) {
			return;
		}
		ChatEmoji emojEentry;
		try {
			for (String str : data) {
				String[] text = str.split(",");
				String fileName = text[0]
						.substring(0, text[0].lastIndexOf("."));
				emojiMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName,
						"drawable", context.getPackageName());

				if (resID != 0) {
					emojEentry = new ChatEmoji();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					emojis.add(emojEentry);
				}
			}
			int pageCount = (int)Math.ceil(emojis.size() / 20);

			for (int i = 0; i < pageCount; i++) {
				emojiLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<ChatEmoji> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;

		if (endIndex > emojis.size()) {
			endIndex = emojis.size();
		}
		List<ChatEmoji> list = new ArrayList<ChatEmoji>();
		list.addAll(emojis.subList(startIndex, endIndex));
		if (list.size() < pageSize) {
			for (int i = list.size(); i < pageSize; i++) {
				ChatEmoji object = new ChatEmoji();
				list.add(object);
			}
		}
		if (list.size() == pageSize) {
			ChatEmoji object = new ChatEmoji();
			object.setId(R.drawable.kf_face_del_icon);
			list.add(object);
		}
		return list;
	}
}