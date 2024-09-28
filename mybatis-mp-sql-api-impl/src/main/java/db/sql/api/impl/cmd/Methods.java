package db.sql.api.impl.cmd;

import db.sql.api.Cmd;
import db.sql.api.cmd.LikeMode;
import db.sql.api.cmd.basic.IParamWrap;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.impl.cmd.basic.*;
import db.sql.api.impl.cmd.condition.*;
import db.sql.api.impl.cmd.dbFun.*;
import db.sql.api.impl.cmd.dbFun.mysql.*;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 数据库方法集合
 */
public final class Methods {

    public MysqlFunctions mysql(Cmd key) {
        return new MysqlFunctions(key);
    }

    public static Cmd paramWrapAndConvertToCmd(Cmd key, Object param) {
        if (java.util.Objects.isNull(param)) {
            return null;
        }
        if (param instanceof Cmd) {
            return (Cmd) param;
        }

        if (!(key instanceof IParamWrap)) {
            return new BasicValue(param);
        }

        IParamWrap paramWrap = (IParamWrap) key;
        return new BasicValue(paramWrap.paramWrap(param));
    }

    public static Object likeParamWrap(Cmd key, Object param, LikeMode mode, boolean isNotLike) {
        if (java.util.Objects.isNull(param)) {
            return null;
        }
        if (param instanceof Cmd) {
            return param;
        }

        if (!(key instanceof IParamWrap)) {
            return param;
        }
        IParamWrap paramWrap = (IParamWrap) key;
        return paramWrap.likeParamWrap(mode, param, isNotLike);
    }

    public static Column column(String column) {
        Objects.requireNonNull(column);
        return new Column(column);
    }

    /**
     * value
     *
     * @param value
     * @return
     */
    public static Cmd cmd(Object value) {
        Objects.requireNonNull(value);
        if (value instanceof Cmd) {
            return (Cmd) value;
        }
        return new BasicValue(value);
    }

    /**
     * value
     *
     * @param value
     * @return
     */
    public static BasicValue value(Serializable value) {
        Objects.requireNonNull(value);
        return new BasicValue(value);
    }

    /**
     * plus加法
     *
     * @param key
     * @param value
     * @return
     */
    public static Plus plus(Cmd key, Number value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Plus(key, value);
    }

    /**
     * plus加法
     *
     * @param key
     * @param value
     * @return
     */
    public static Plus plus(Cmd key, Cmd value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Plus(key, value);
    }

    /**
     * subtract加法
     *
     * @param key
     * @param value
     * @return
     */
    public static Subtract subtract(Cmd key, Number value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Subtract(key, value);
    }

    /**
     * subtract减法
     *
     * @param key
     * @param value
     * @return
     */
    public static Subtract subtract(Cmd key, Cmd value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Subtract(key, value);
    }

    /**
     * multiply乘法
     *
     * @param key
     * @param value
     * @return
     */
    public static Multiply multiply(Cmd key, Number value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Multiply(key, value);
    }

    /**
     * divide除法
     *
     * @param key
     * @param value
     * @return
     */
    public static Divide divide(Cmd key, Cmd value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Divide(key, value);
    }

    /**
     * multiply乘法
     *
     * @param key
     * @param value
     * @return
     */
    public static Divide divide(Cmd key, Number value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Divide(key, value);
    }

    /**
     * multiply乘法
     *
     * @param key
     * @param value
     * @return
     */
    public static Multiply multiply(Cmd key, Cmd value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Multiply(key, value);
    }

    /**
     * count条数 函数
     *
     * @param key
     * @return
     */
    public static Count count(Cmd key) {
        Objects.requireNonNull(key);
        return new Count(key);
    }

    /**
     * count(1) 条数 函数
     *
     * @return
     */
    public static Count1 count1() {
        return Count1.INSTANCE;
    }

    /**
     * count(*) 条数 函数
     *
     * @return
     */
    public static CountAll countAll() {
        return CountAll.INSTANCE;
    }

    /**
     * count条数 函数
     *
     * @param key
     * @param distinct 是否去重
     * @return
     */
    public static Count count(Cmd key, boolean distinct) {
        Objects.requireNonNull(key);
        return new Count(key, distinct);
    }

    /**
     * sum求和 函数
     *
     * @param key
     * @return
     */
    public static Sum sum(Cmd key) {
        Objects.requireNonNull(key);
        return new Sum(key);
    }

    /**
     * min最小 函数
     *
     * @param key
     * @return
     */
    public static Min min(Cmd key) {
        Objects.requireNonNull(key);
        return new Min(key);
    }

    /**
     * max最大 函数
     *
     * @param key
     * @return
     */
    public static Max max(Cmd key) {
        Objects.requireNonNull(key);
        return new Max(key);
    }

    /**
     * avg平局值 函数
     *
     * @param key
     * @return
     */
    public static Avg avg(Cmd key) {
        Objects.requireNonNull(key);
        return new Avg(key);
    }

    /**
     * abs绝对值 函数
     *
     * @param key
     * @return
     */
    public static Abs abs(Cmd key) {
        Objects.requireNonNull(key);
        return new Abs(key);
    }

    /**
     * pow平方 函数
     *
     * @param key
     * @param n
     * @return
     */
    public static Pow pow(Cmd key, int n) {
        Objects.requireNonNull(key);
        return new Pow(key, n);
    }

    /**
     * round四舍五入 取整数位 函数
     *
     * @param key
     * @return
     */
    public static Round round(Cmd key) {
        return round(key, 0);
    }

    /**
     * round四舍五入 函数
     *
     * @param key
     * @param precision 精度
     * @return
     */
    public static Round round(Cmd key, int precision) {
        Objects.requireNonNull(key);
        return new Round(key, precision);
    }


    /**
     * ceil返回大于或等于 x 的最小整数（向上取整） 函数
     *
     * @param key
     * @return
     */
    public static Ceil ceil(Cmd key) {
        Objects.requireNonNull(key);
        return new Ceil(key);
    }

    /**
     * floor返回小于或等于 x 的最大整数（向下取整） 函数
     *
     * @param key
     * @return
     */
    public static Floor floor(Cmd key) {
        Objects.requireNonNull(key);
        return new Floor(key);
    }

    /**
     * rand返回 0~1 的随机数 函数
     *
     * @param key
     * @return
     */
    public static Rand rand(Cmd key) {
        Objects.requireNonNull(key);
        return new Rand(key);
    }

    /**
     * rand返回 0~max 的随机数 函数
     *
     * @param key
     * @param max
     * @return
     */
    public static Rand rand(Cmd key, Number max) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(max);
        return new Rand(key, max);
    }

    /**
     * sign 返回 key 的符号，key 是负数、0、正数分别返回 -1、0、1 函数
     *
     * @param key
     * @return
     */
    public static Sign sign(Cmd key) {
        Objects.requireNonNull(key);
        return new Sign(key);
    }

    /**
     * pi 返回圆周率 函数
     *
     * @return
     */
    public static Pi pi() {
        return new Pi();
    }

    /**
     * 返回数值 key 整数位 函数
     *
     * @param key
     * @return
     */
    public static Truncate truncate(Cmd key) {
        Objects.requireNonNull(key);
        return truncate(key, 0);
    }

    /**
     * 返回数值 key 保留到小数点后 precision 位的值 函数
     *
     * @param key
     * @param precision
     * @return
     */
    public static Truncate truncate(Cmd key, int precision) {
        Objects.requireNonNull(key);
        return new Truncate(key, precision);
    }

    /**
     * sqrt 平方根 函数
     *
     * @param key
     * @return
     */
    public static Sqrt sqrt(Cmd key) {
        Objects.requireNonNull(key);
        return new Sqrt(key);
    }

    /**
     * mod 取模 函数
     *
     * @param key
     * @return
     */
    public static Mod mod(Cmd key, Number number) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(number);
        return new Mod(key, number);
    }

    /**
     * 返回 e 的 key 次方 函数
     *
     * @param key
     * @return
     */
    public static Exp exp(Cmd key) {
        Objects.requireNonNull(key);
        return new Exp(key);
    }


    /**
     * 返回自然对数（以 e 为底的对数） 函数
     *
     * @param key
     * @return
     */
    public static Log log(Cmd key) {
        Objects.requireNonNull(key);
        return new Log(key);
    }

    /**
     * 返回以 2 为底的对数 函数
     *
     * @param key
     * @return
     */
    public static Log2 log2(Cmd key) {
        Objects.requireNonNull(key);
        return new Log2(key);
    }

    /**
     * 返回以 10 为底的对数 函数
     *
     * @param key
     * @return
     */
    public static Log10 log10(Cmd key) {
        Objects.requireNonNull(key);
        return new Log10(key);
    }

    /**
     * 将弧度转换为角度 函数
     *
     * @param key
     * @return
     */
    public static Degrees degrees(Cmd key) {
        Objects.requireNonNull(key);
        return new Degrees(key);
    }

    /**
     * 将角度转换为弧度 函数
     *
     * @param key
     * @return
     */
    public static Radians radians(Cmd key) {
        Objects.requireNonNull(key);
        return new Radians(key);
    }

    /**
     * 求正弦值 函数
     *
     * @param key
     * @return
     */
    public static Sin sin(Cmd key) {
        Objects.requireNonNull(key);
        return new Sin(key);
    }

    /**
     * 求反正弦值 函数
     *
     * @param key
     * @return
     */
    public static Asin asin(Cmd key) {
        Objects.requireNonNull(key);
        return new Asin(key);
    }

    /**
     * 求余弦值 函数
     *
     * @param key
     * @return
     */
    public static Cos cos(Cmd key) {
        Objects.requireNonNull(key);
        return new Cos(key);
    }

    /**
     * 求反余弦值 函数
     *
     * @param key
     * @return
     */
    public static Acos acos(Cmd key) {
        Objects.requireNonNull(key);
        return new Acos(key);
    }

    /**
     * 求正切值 函数
     *
     * @param key
     * @return
     */
    public static Tan tan(Cmd key) {
        Objects.requireNonNull(key);
        return new Tan(key);
    }

    /**
     * 求反正切值 函数
     *
     * @param key
     * @return
     */
    public static Atan atan(Cmd key) {
        Objects.requireNonNull(key);
        return new Atan(key);
    }

    /**
     * 求余切值 函数
     *
     * @param key
     * @return
     */
    public static Cot cot(Cmd key) {
        Objects.requireNonNull(key);
        return new Cot(key);
    }

    /**
     * 返回字符串的字符数
     *
     * @param key
     * @return
     */
    public static CharLength charLength(Cmd key) {
        Objects.requireNonNull(key);
        return new CharLength(key);
    }

    /**
     * 返回字符串的长度 函数
     *
     * @param key
     * @return
     */
    public static Length length(Cmd key) {
        Objects.requireNonNull(key);
        return new Length(key);
    }

    /**
     * 转换成大写 函数
     *
     * @param key
     * @return
     */
    public static Upper upper(Cmd key) {
        Objects.requireNonNull(key);
        return new Upper(key);
    }

    /**
     * 转换成小写 函数
     *
     * @param key
     * @return
     */
    public static Lower lower(Cmd key) {
        Objects.requireNonNull(key);
        return new Lower(key);
    }

    /**
     * 左边截取
     *
     * @param key
     * @return
     */
    public static Left left(Cmd key, int length) {
        Objects.requireNonNull(key);
        return new Left(key, length);
    }

    /**
     * 右边截取
     *
     * @param key
     * @return
     */
    public static Right right(Cmd key, int start) {
        Objects.requireNonNull(key);
        return new Right(key, start);
    }

    /**
     * 字符截取
     *
     * @param key
     * @param start
     * @return SubStr
     */
    public static SubStr subStr(Cmd key, int start) {
        Objects.requireNonNull(key);
        return new SubStr(key, start);
    }

    /**
     * 字符截取
     *
     * @param key
     * @param start
     * @param length
     * @return SubStr
     */
    public static SubStr subStr(Cmd key, int start, int length) {
        Objects.requireNonNull(key);
        return new SubStr(key, start, length);
    }

    /**
     * 从左边开始填充
     *
     * @param key
     * @param length
     * @param pad
     * @return
     */
    public static Lpad lpad(Cmd key, int length, String pad) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(pad);
        return new Lpad(key, length, pad);
    }

    /**
     * 从左边开始填充
     *
     * @param key
     * @param length
     * @param pad
     * @return
     */
    public static Rpad rpad(Cmd key, int length, String pad) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(pad);
        return new Rpad(key, length, pad);
    }

    /**
     * 删除两边空格
     *
     * @param key
     * @return
     */
    public static Trim trim(Cmd key) {
        Objects.requireNonNull(key);
        return new Trim(key);
    }

    /**
     * 删除左边空格
     *
     * @param key
     * @return
     */
    public static Ltrim ltrim(Cmd key) {
        Objects.requireNonNull(key);
        return new Ltrim(key);
    }

    /**
     * 删除右边空格
     *
     * @param key
     * @return
     */
    public static Rtrim rtrim(Cmd key) {
        Objects.requireNonNull(key);
        return new Rtrim(key);
    }

    /**
     * 字符串比较 函数
     * 返回 -1 0 1
     *
     * @param key
     * @param str
     * @return
     */
    public static Strcmp strcmp(Cmd key, String str) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(str);
        return new Strcmp(key, str);
    }

    /**
     * 将字符串  重复 n 次
     *
     * @param key
     * @param n
     * @return
     */
    public static Repeat repeat(Cmd key, int n) {
        Objects.requireNonNull(key);
        return new Repeat(key, n);
    }


    /**
     * 替换 函数
     *
     * @param key
     * @param target      匹配目标
     * @param replacement 替换值
     * @return
     */
    public static Replace replace(Cmd key, String target, String replacement) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(target);
        Objects.requireNonEmpty(replacement);
        return new Replace(key, target, replacement);
    }

    /**
     * 反转函数
     *
     * @param key
     * @return
     */
    public static Reverse reverse(Cmd key) {
        Objects.requireNonNull(key);
        return new Reverse(key);
    }

    /**
     * 匹配 match 在 key里边的位置
     * key 需要符合逗号分割规范
     *
     * @param key
     * @param match
     * @return
     */
    public static FindInSet mysqlFindInSet(Cmd key, String match) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(match);
        return new FindInSet(key, match);
    }

    /**
     * 匹配 match 在 key里边的位置
     * key 需要符合逗号分割规范
     *
     * @param key
     * @param match
     * @return
     */
    public static FindInSet mysqlFindInSet(Cmd key, Number match) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(match);
        return new FindInSet(key, match + "");
    }

    /**
     * 匹配key 在values里的位置 从1 开始
     *
     * @param key
     * @param values 数据
     * @return
     */
    @SafeVarargs
    public static Field mysqlFiled(Cmd key, Object... values) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(values);
        return new Field(key, values);
    }

    /**
     * 当前日期
     *
     * @return
     */
    public static CurrentDate currentDate() {
        return new CurrentDate();
    }

    /**
     * 当前时间（不包含日期）
     *
     * @return
     */
    public static CurrentTime currentTime() {
        return new CurrentTime();
    }

    /**
     * 当前时间（包含日期）
     *
     * @return
     */
    public static CurrentDateTime currentDateTime() {
        return new CurrentDateTime();
    }

    /**
     * 获取年份
     *
     * @param key
     * @return
     */
    public static Year year(Cmd key) {
        Objects.requireNonNull(key);
        return new Year(key);
    }

    /**
     * 获取月份
     *
     * @param key
     * @return
     */
    public static Month month(Cmd key) {
        Objects.requireNonNull(key);
        return new Month(key);
    }

    /**
     * 获取日期部分，不包含时分秒
     *
     * @param key
     * @return
     */
    public static DateFormat date(Cmd key) {
        Objects.requireNonNull(key);
        return new DateFormat(key, DatePattern.YYYY_MM_DD);
    }

    /**
     * 格式化时间
     *
     * @param key
     * @param pattern
     * @return
     */
    public static DateFormat dateFormat(Cmd key, String pattern) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(pattern);
        return new DateFormat(key, pattern);
    }

    /**
     * 格式化时间
     *
     * @param key
     * @param pattern
     * @return
     */
    public static DateFormat dateFormat(Cmd key, DatePattern pattern) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(pattern);
        return new DateFormat(key, pattern);
    }

    /**
     * 获取第几天
     *
     * @param key
     * @return
     */
    public static Day day(Cmd key) {
        Objects.requireNonNull(key);
        return new Day(key);
    }

    /**
     * 获取星期几
     *
     * @param key
     * @return
     */
    public static Weekday weekday(Cmd key) {
        Objects.requireNonNull(key);
        return new Weekday(key);
    }


    /**
     * 获取小时
     *
     * @param key
     * @return
     */
    public static Hour hour(Cmd key) {
        Objects.requireNonNull(key);
        return new Hour(key);
    }

    /**
     * 日期比较
     *
     * @param key
     * @return
     */
    public static DateDiff dateDiff(Cmd key, Cmd another) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(another);
        return new DateDiff(key, another);
    }

    /**
     * 日期增加
     *
     * @param key
     * @return
     */
    public static DateAdd dateAdd(Cmd key, int n, TimeUnit timeUnit) {
        Objects.requireNonNull(key);
        return new DateAdd(key, n, timeUnit);
    }

    /**
     * md5
     *
     * @param str
     * @return
     */
    public static Md5 mysqlMd5(String str) {
        Objects.requireNonEmpty(str);
        return new Md5(str);
    }

    /**
     * md5
     *
     * @param key
     * @return
     */
    public static Md5 mysqlMd5(Cmd key) {
        Objects.requireNonNull(key);
        return new Md5(key);
    }

    /**
     * 将ip转成数字
     *
     * @param ip
     * @return
     */
    public static InetAton inetAton(String ip) {
        Objects.requireNonEmpty(ip);
        return new InetAton(ip);
    }

    /**
     * 将ip转成数字
     *
     * @param key
     * @return
     */
    public static InetAton inetAton(Cmd key) {
        Objects.requireNonNull(key);
        return new InetAton(key);
    }

    /**
     * 将ip数字转成ip
     *
     * @param ipNumber
     * @return
     */
    public static InetNtoa inetNtoa(Number ipNumber) {
        Objects.requireNonNull(ipNumber);
        return new InetNtoa(ipNumber);
    }

    /**
     * 将ip数字转成ip
     *
     * @param key
     * @return
     */
    public static InetNtoa inetNtoa(Cmd key) {
        Objects.requireNonNull(key);
        return new InetNtoa(key);
    }

    /**
     * concat拼接 函数
     *
     * @param key
     * @param values 数据
     * @return
     */
    @SafeVarargs
    public static Concat concat(Cmd key, Object... values) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(values);
        return new Concat(key, values);
    }

    /**
     * concatAs拼接 函数
     *
     * @param key
     * @param split
     * @param values
     * @return
     */
    @SafeVarargs
    public static ConcatAs concatAs(Cmd key, String split, Object... values) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(split);
        Objects.requireNonNull(values);
        return new ConcatAs(key, split, values);
    }

    /**
     * if(条件,值1,值2) 函数
     *
     * @param condition
     * @param value
     * @param thenValue
     * @return
     */
    public static If if_(Condition condition, Serializable value, Serializable thenValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(thenValue);
        Objects.requireNonNull(condition);
        return new If(condition, value, thenValue);
    }

    /**
     * if(条件,值1,值2) 函数
     *
     * @param condition
     * @param value
     * @param thenValue
     * @return
     */
    public static If if_(Condition condition, Cmd value, Serializable thenValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(thenValue);
        Objects.requireNonNull(condition);
        return new If(condition, value, thenValue);
    }

    /**
     * if(条件,值1,值2) 函数
     *
     * @param condition
     * @param value
     * @param thenValue
     * @return
     */
    public static If if_(Condition condition, Serializable value, Cmd thenValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(thenValue);
        Objects.requireNonNull(condition);
        return new If(condition, value, thenValue);
    }

    /**
     * IF(条件,值1,值2) 函数
     *
     * @param condition
     * @param value
     * @param thenValue
     * @return
     */
    public static If if_(Condition condition, Cmd value, Cmd thenValue) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(thenValue);
        Objects.requireNonNull(condition);
        return new If(condition, value, thenValue);
    }

    /**
     * IFNULL(条件,值1,值2) 函数
     *
     * @param key
     * @param value
     * @return
     */
    public static IfNull ifNull(Cmd key, Cmd value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new IfNull(key, value);
    }

    /**
     * IFNULL(条件,值1,值2) 函数
     *
     * @param key
     * @param value
     * @return
     */
    public static IfNull ifNull(Cmd key, Serializable value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new IfNull(key, value);
    }

    /**
     * key列 is NOT NULL
     *
     * @param key
     * @return
     */
    public static IsNull isNull(Cmd key) {
        Objects.requireNonNull(key);
        return new IsNull(key);
    }

    /**
     * key列 is NOT NULL
     *
     * @param key
     * @return
     */
    public static IsNotNull isNotNull(Cmd key) {
        Objects.requireNonNull(key);
        return new IsNotNull(key);
    }

    /**
     * key列 为空
     *
     * @param key
     * @return
     */
    public static Eq isEmpty(Cmd key) {
        Objects.requireNonNull(key);
        return new Eq(key, SqlConst.EMPTY);
    }

    /**
     * key列 不为空
     *
     * @param key
     * @return
     */
    public static Ne isNotEmpty(Cmd key) {
        Objects.requireNonNull(key);
        return new Ne(key, SqlConst.EMPTY);
    }

    /**
     * case 语句块
     *
     * @return
     */
    public static Case case_() {
        return new Case();
    }

    /* --------------------------------------以下为判断条件----------------------------------------------*/

    /**
     * eq等于 判断
     *
     * @return
     */
    public static Eq eq(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Eq(key, paramWrapAndConvertToCmd(key, value));
    }

    /**
     * ne不等于 判断
     *
     * @return
     */
    public static Ne ne(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Ne(key, paramWrapAndConvertToCmd(key, value));
    }

    /**
     * 不为空 判断
     *
     * @return
     */
    public static NotEmpty notEmpty(Cmd key) {
        Objects.requireNonNull(key);
        return new NotEmpty(key);
    }

    /**
     * 为空 判断
     *
     * @return
     */
    public static Empty empty(Cmd key) {
        Objects.requireNonNull(key);
        return new Empty(key);
    }


    /**
     * gt大于 判断
     *
     * @return
     */
    public static Gt gt(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Gt(key, paramWrapAndConvertToCmd(key, value));
    }


    /**
     * gte大于等于 判断
     *
     * @return
     */
    public static Gte gte(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Gte(key, paramWrapAndConvertToCmd(key, value));
    }

    /**
     * gt小于 判断
     *
     * @return
     */
    public static Lt lt(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Lt(key, paramWrapAndConvertToCmd(key, value));
    }

    /**
     * gt小于等于 判断
     *
     * @return
     */
    public static Lte lte(Cmd key, Object value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        return new Lte(key, paramWrapAndConvertToCmd(key, value));
    }


    /**
     * in 多个值
     *
     * @return
     */
    @SafeVarargs
    public static In in(Cmd key, Object... values) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(values);
        Cmd[] cmds = new Cmd[values.length];
        for (int i = 0; i < values.length; i++) {
            cmds[i] = paramWrapAndConvertToCmd(key, values[i]);
        }
        return new In(key).add(cmds);
    }

    /**
     * in 多个值
     *
     * @return
     */
    public static In in(Cmd key, Collection<?> values) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(values);
        return new In(key).add(values.stream().map(item -> paramWrapAndConvertToCmd(key, item)).collect(Collectors.toList()));
    }

    /**
     * in 一个查询
     *
     * @return
     */
    public static In in(Cmd key, IQuery query) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(query);
        return new In(key).add(query);
    }

    /**
     * not in 多个值
     *
     * @return
     */
    @SafeVarargs
    public static NotIn notIn(Cmd key, Object... values) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(values);
        Cmd[] cmds = new Cmd[values.length];
        for (int i = 0; i < values.length; i++) {
            cmds[i] = paramWrapAndConvertToCmd(key, values[i]);
        }
        return new NotIn(key).add(cmds);
    }

    /**
     * not in 多个值
     *
     * @return
     */
    public static NotIn notIn(Cmd key, Collection<?> values) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(values);
        return new NotIn(key).add(values.stream().map(item -> paramWrapAndConvertToCmd(key, item)).collect(Collectors.toList()));
    }

    /**
     * not in 一个查询
     *
     * @return
     */
    public static NotIn notIn(Cmd key, IQuery query) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(query);
        return new NotIn(key).add(query);
    }

    /**
     * exists 一个查询
     *
     * @param query
     * @return
     */
    public static Exists exists(IQuery query) {
        Objects.requireNonNull(query);
        return new Exists(query);
    }

    /**
     * not exists 一个查询
     *
     * @param query
     * @return
     */
    public static NotExists notExists(IQuery query) {
        Objects.requireNonNull(query);
        return new NotExists(query);
    }

    /**
     * between 区间判断
     *
     * @param key
     * @param value
     * @param value2
     * @return
     */
    public static Between between(Cmd key, Serializable value, Serializable value2) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        Objects.requireNonNull(value2);
        return new Between(key, paramWrapAndConvertToCmd(key, value), paramWrapAndConvertToCmd(key, value2));
    }

    /**
     * not between 区间判断
     *
     * @param key
     * @param value
     * @param value2
     * @return
     */
    public static NotBetween notBetween(Cmd key, Serializable value, Serializable value2) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        Objects.requireNonNull(value2);
        return new NotBetween(key, paramWrapAndConvertToCmd(key, value), paramWrapAndConvertToCmd(key, value2));
    }

    /**
     * like 判断
     *
     * @param key
     * @param value
     * @return
     */
    public static Like like(Cmd key, Object value) {
        return like(LikeMode.DEFAULT, key, value);
    }

    /**
     * like 判断
     *
     * @param key
     * @param value
     * @return
     */
    public static Like like(LikeMode mode, Cmd key, Object value) {
        Objects.requireNonNull(key);
        Object wrapValue = likeParamWrap(key, value, mode, false);
        if (wrapValue instanceof Object[]) {
            Object[] values = (Object[]) wrapValue;
            mode = (LikeMode) values[0];
            value = values[1];
        }
        return new Like(mode, key, value);
    }

    /**
     * notLike 判断
     *
     * @param key
     * @param value
     * @return
     */
    public static NotLike notLike(Cmd key, Object value) {
        return notLike(LikeMode.DEFAULT, key, value);
    }

    /**
     * not like 判断
     *
     * @param key
     * @param value
     * @return
     */
    public static NotLike notLike(LikeMode mode, Cmd key, Object value) {
        Objects.requireNonNull(key);
        Object wrapValue = likeParamWrap(key, value, mode, true);
        if (wrapValue instanceof Object[]) {
            Object[] values = (Object[]) wrapValue;
            mode = (LikeMode) values[0];
            value = values[1];
        }
        return new NotLike(mode, key, value);
    }

    /**
     * mysql fromUnixTime 函数
     *
     * @param key
     * @return FromUnixTime
     */
    public static FromUnixTime mysqlFromUnixTime(Cmd key) {
        Objects.requireNonNull(key);
        return new FromUnixTime(key);
    }

    /**
     * mysql fromUnixTime 函数
     *
     * @param key          列
     * @param containValue 包含值
     * @return FromUnixTime
     */
    public static JsonContains mysqlJsonContains(Cmd key, Serializable containValue) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(containValue);
        return new JsonContains(key, containValue);
    }

    /**
     * mysql json 是否包含值
     *
     * @param key          列
     * @param containValue 包含值
     * @param path         指定路径
     * @return FromUnixTime
     */
    public static JsonContains mysqlJsonContains(Cmd key, Serializable containValue, String path) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(containValue);
        Objects.requireNonEmpty(path);
        return new JsonContains(key, containValue, path);
    }

    /**
     * mysql json 是否包含路径
     *
     * @param key   列
     * @param paths 指定路径
     * @return
     */
    @SafeVarargs
    public static JsonContainsPath mysqlJsonContainsPath(Cmd key, String... paths) {
        return mysqlJsonContainsPath(key, true, paths);
    }

    /**
     * mysql json 是否包含路径
     *
     * @param key      列
     * @param allMatch 是否全匹配上
     * @param paths    指定路径
     * @return
     */
    @SafeVarargs
    public static JsonContainsPath mysqlJsonContainsPath(Cmd key, boolean allMatch, String... paths) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(paths);
        return new JsonContainsPath(key, allMatch, paths);
    }

    @SafeVarargs
    public static JsonExtract mysqlJsonExtract(Cmd key, String... paths) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(paths);
        return new JsonExtract(key, paths);
    }

    /**
     * 返回目标字符(串)在源字符串字符串中出现的起始位置
     *
     * @param key 列
     * @param str 匹配的字符
     * @return
     */
    public static Instr instr(Cmd key, String str) {
        Objects.requireNonNull(key);
        Objects.requireNonEmpty(str);
        return new Instr(key, str);
    }
}
