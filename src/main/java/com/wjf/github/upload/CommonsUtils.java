package com.wjf.github.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CommonsUtils {

	public static void main(String[] args) {
		System.out.println(CommonsUtils.getUUID());
	}

	// 生成UUID的方法
	public static String getUUID() {
		return UUID.randomUUID().toString().trim().replace("-", "");
	}

	// 文件上传后返回相对路径的方法
	public static String getUploadfilepath(MultipartFile file, String realPath, String... path)
			throws IllegalStateException, IOException {
		// 获取文件名
		String filename = file.getOriginalFilename();
		// 获取文件后缀名
		String extensionname = filename.substring(filename.lastIndexOf(".") + 1);
		// 给上传的文件起别名，有很多种方式
		String newFilename = CommonsUtils.getUUID() + "." + extensionname;

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd");

		String localPath = null;

		if (path != null && path.length > 0) {
			localPath = path[0];
		} else if ("BMP".equals(extensionname.toUpperCase()) || "JPG".equals(extensionname.toUpperCase())
				|| "JPEG".equals(extensionname.toUpperCase()) || "PNG".equals(extensionname.toUpperCase())
				|| "GIF".equals(extensionname.toUpperCase())) {
			localPath = "imgs/";
		} else {
			localPath = "files/";
		}

		localPath = localPath + sdf1.format(new Date()) + "/";
		localPath = localPath + sdf2.format(new Date()) + "/";

		File dirs = new File(realPath + localPath);

		if (!dirs.exists()) {
			dirs.mkdirs();
		}

		// 创建File对象，传入目标路径参数，和新的文件别名
		File dir = new File(realPath + localPath, newFilename);
		// 如果dir代表的文件不存在，则创建它，
		if (!dir.exists()) {
			dir.mkdirs();
		}
		file.transferTo(dir);
		return localPath + newFilename;
	}

//-----------------------------------------------------------------------------------------------------------------------

	/**
	 * 随机生成一段字母加数字 length是生成字符的长度
	 *
	 * @param length
	 * @return
	 */
	public static String getStringRandom(int length) {
		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 随机生成姓名
	 *
	 * @return
	 */
	public static String getName() {
		Random random = new Random();
		String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦",
				"尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦",
				"章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳",
				"酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常", "乐",
				"于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵",
				"湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
				"屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季"};
		String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
		String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
		int index = random.nextInt(Surname.length - 1);
		String name = Surname[index];
		int i = random.nextInt(3);
		if (i == 2) {
			int j = random.nextInt(girl.length() - 2);
			if (j % 2 == 0) {
				name = name + girl.substring(j, j + 2);
			} else {
				name = name + girl.substring(j, j + 1);
			}

		} else {
			int j = random.nextInt(girl.length() - 2);
			if (j % 2 == 0) {
				name = name + boy.substring(j, j + 2);
			} else {
				name = name + boy.substring(j, j + 1);
			}

		}
		return name;
	}

	/**
	 * 随机生成电话号码
	 *
	 * @return
	 */
	public static String getMobileNo() {
		String mobileNo = startMobileNo() + endMobileNo();
		return mobileNo;
	}

	// 随机生成电话号码前三个数字
	public static int startMobileNo() {
		int[] mobileStart = {139, 138, 137, 136, 135, 134, 159, 158, 157, 150, 151, 152, 188, 130, 131, 132, 156, 155,
				133, 153, 189, 180, 177, 176};
		Random r = new Random();
		ArrayList<Integer> mobileList = new ArrayList<>();
		for (int i = 0; i < mobileStart.length; i++) {
			mobileList.add(mobileStart[i]);
		}
		return mobileList.get(r.nextInt(mobileList.size()));
	}

	// 随机生成电话号码后八个数字
	public static String endMobileNo() {
		Random r = new Random();
		String temp = "";
		for (int i = 0; i < 8; i++) {
			temp += r.nextInt(10);
		}
		return temp;
	}

	/**
	 * 随机生成文字参数length就是文字长度
	 *
	 * @param length
	 * @return
	 */
	public static String getChineseString(int length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i <= length; i++) {
			str.append(getRandomChar());
		}
		return str.toString();
	}

	// 随机生成一个文字
	public static char getRandomChar() {
		String str = "";
		int hightPos; //
		int lowPos;
		Random random = new Random();
		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));
		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();
		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("错误");
		}
		return str.charAt(0);
	}
}
