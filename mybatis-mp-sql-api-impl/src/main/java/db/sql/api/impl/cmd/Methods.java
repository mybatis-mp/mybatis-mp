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

    /**
     * 参数包装并转成CMD对象
     *
     * @param column 列
     * @param param  参数
     * @return Cmd
     */
    public static Cmd paramWrapAndConvertToCmd(Cmd column, Object param) {
        if (java.util.Objects.isNull(param)) {
            return null;
        }
        if (param instanceof Cmd) {
            return (Cmd) param;
        }

        if (!(column instanceof IParamWrap)) {
            return new BasicValue(param);
        }

        IParamWrap paramWrap = (IParamWrap) column;
        return new BasicValue(paramWrap.paramWrap(param));
    }

    /**
     * LIKE 参数包装
     *
     * @param column    列
     * @param param     参数
     * @param mode      like方式
     * @param isNotLike 是否为NOT LIKE
     * @return param包装后的值
     */
    public static Object likeParamWrap(Cmd column, Object param, LikeMode mode, boolean isNotLike) {
        if (java.util.Objects.isNull(param)) {
            return null;
        }
        if (param instanceof Cmd) {
            return param;
        }

        if (!(column instanceof IParamWrap)) {
            return param;
        }
        IParamWrap paramWrap = (IParamWrap) column;
        return paramWrap.likeParamWrap(mode, param, isNotLike);
    }

    /**
     * 将指定列名转成 Column
     * @param column 列名
     * @return Column
     */
    public static Column column(String column) {
        Objects.requireNonNull(column);
        return new Column(column);
    }

    /**
     * 将value转成Cmd对象；普通值将转成为BasicValue
     *
     * @param value 值
     * @return Cmd
     */
    public static Cmd cmd(Object value) {
        Objects.requireNonNull(value);
        if (value instanceof Cmd) {
            return (Cmd) value;
        }
        return new BasicValue(value);
    }

    /**
     * 将普通值转换为BasicValue
     *
     * @param value 值
     * @return BasicValue
     */
    public static BasicValue value(Serializable value) {
        Objects.requireNonNull(value);
        return new BasicValue(value);
    }

    /**
     * plus加法
     *
     * @param column 列
     * @param value 值
     * @return Plus
     */
    public static Plus plus(Cmd column, Number value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Plus(column, value);
    }

    /**
     * plus加法
     *
     * @param column 列
     * @param value 值
     * @return Plus
     */
    public static Plus plus(Cmd column, Cmd value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Plus(column, value);
    }

    /**
     * subtract加法
     *
     * @param column 列
     * @param value 值
     * @return Subtract
     */
    public static Subtract subtract(Cmd column, Number value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Subtract(column, value);
    }

    /**
     * subtract减法
     *
     * @param column 列
     * @param value 值
     * @return Subtract
     */
    public static Subtract subtract(Cmd column, Cmd value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Subtract(column, value);
    }

    /**
     * multiply乘法
     *
     * @param column 列
     * @param value 值
     * @return Multiply
     */
    public static Multiply multiply(Cmd column, Number value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Multiply(column, value);
    }

    /**
     * divide除法
     *
     * @param column 列
     * @param value 值
     * @return Divide
     */
    public static Divide divide(Cmd column, Cmd value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Divide(column, value);
    }

    /**
     * multiply乘法
     *
     * @param column 列
     * @param value 值
     * @return Divide
     */
    public static Divide divide(Cmd column, Number value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Divide(column, value);
    }

    /**
     * multiply乘法
     *
     * @param column 列
     * @param value 值
     * @return Multiply
     */
    public static Multiply multiply(Cmd column, Cmd value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Multiply(column, value);
    }

    /**
     * count条数 函数
     *
     * @param column 列
     * @return Count
     */
    public static Count count(Cmd column) {
        Objects.requireNonNull(column);
        return new Count(column);
    }

    /**
     * count(1) 条数 函数
     *
     * @return Count1
     */
    public static Count1 count1() {
        return Count1.INSTANCE;
    }

    /**
     * count(*) 条数 函数
     *
     * @return CountAll
     */
    public static CountAll countAll() {
        return CountAll.INSTANCE;
    }

    /**
     * count条数 函数
     *
     * @param column 列
     * @param distinct 是否去重
     * @return Count
     */
    public static Count count(Cmd column, boolean distinct) {
        Objects.requireNonNull(column);
        return new Count(column, distinct);
    }

    /**
     * sum求和 函数
     *
     * @param column 列
     * @return Sum
     */
    public static Sum sum(Cmd column) {
        Objects.requireNonNull(column);
        return new Sum(column);
    }

    /**
     * min最小 函数
     *
     * @param column 列
     * @return Min
     */
    public static Min min(Cmd column) {
        Objects.requireNonNull(column);
        return new Min(column);
    }

    /**
     * max最大 函数
     *
     * @param column 列
     * @return Max
     */
    public static Max max(Cmd column) {
        Objects.requireNonNull(column);
        return new Max(column);
    }

    /**
     * avg平局值 函数
     *
     * @param column 列
     * @return Avg
     */
    public static Avg avg(Cmd column) {
        Objects.requireNonNull(column);
        return new Avg(column);
    }

    /**
     * abs绝对值 函数
     *
     * @param column 列
     * @return Abs
     */
    public static Abs abs(Cmd column) {
        Objects.requireNonNull(column);
        return new Abs(column);
    }

    /**
     * pow平方 函数
     *
     * @param column 列
     * @param n 次数
     * @return Pow
     */
    public static Pow pow(Cmd column, int n) {
        Objects.requireNonNull(column);
        return new Pow(column, n);
    }

    /**
     * round四舍五入 取整数位 函数
     *
     * @param column 列
     * @return
     */
    public static Round round(Cmd column) {
        return round(column, 0);
    }

    /**
     * round四舍五入 函数
     *
     * @param column 列
     * @param precision 精度
     * @return
     */
    public static Round round(Cmd column, int precision) {
        Objects.requireNonNull(column);
        return new Round(column, precision);
    }

    /**
     * ceil返回大于或等于 x 的最小整数（向上取整） 函数
     *
     * @param column 列
     * @return
     */
    public static Ceil ceil(Cmd column) {
        Objects.requireNonNull(column);
        return new Ceil(column);
    }

    /**
     * floor返回小于或等于 x 的最大整数（向下取整） 函数
     *
     * @param column 列
     * @return
     */
    public static Floor floor(Cmd column) {
        Objects.requireNonNull(column);
        return new Floor(column);
    }

    /**
     * 0~1 的随机数 函数
     *
     * @return Rand
     */
    public static Rand rand() {
        return new Rand();
    }

    /**
     * 0~1 的随机数 函数
     *
     * @param n 随机因子
     * @return Rand
     */
    public static Rand rand(Number n) {
        return new Rand(n);
    }

    /**
     * 获取 column的符号，负数为-1 0为0、正数为1
     *
     * @param column 列
     * @return
     */
    public static Sign sign(Cmd column) {
        Objects.requireNonNull(column);
        return new Sign(column);
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
     * 返回数值 column 整数位 函数
     *
     * @param column 列
     * @return
     */
    public static Truncate truncate(Cmd column) {
        Objects.requireNonNull(column);
        return truncate(column, 0);
    }

    /**
     * 返回数值 column 保留到小数点后 precision 位的值 函数
     *
     * @param column 列
     * @param precision 精度
     * @return
     */
    public static Truncate truncate(Cmd column, int precision) {
        Objects.requireNonNull(column);
        return new Truncate(column, precision);
    }

    /**
     * sqrt 平方根 函数
     *
     * @param column 列
     * @return
     */
    public static Sqrt sqrt(Cmd column) {
        Objects.requireNonNull(column);
        return new Sqrt(column);
    }

    /**
     * mod 取模 函数
     *
     * @param column 列
     * @return
     */
    public static Mod mod(Cmd column, Number divisor) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(divisor);
        return new Mod(column, divisor);
    }

    /**
     * 返回 e 的 n 次方 函数
     *
     * @param n 次方
     * @return Exp
     */
    public static Exp exp(int n) {
        return new Exp(n);
    }

    /**
     * 以base为底，求number自然对数
     *
     * @param base 底数
     * @param number 对数
     * @return Log
     */
    public static Log log(Cmd base, int number) {
        Objects.requireNonNull(base);
        return new Log(base, number);
    }

    /**
     * 将弧度转换为角度 函数
     *
     * @param column 列
     * @return Degrees
     */
    public static Degrees degrees(Cmd column) {
        Objects.requireNonNull(column);
        return new Degrees(column);
    }

    /**
     * 将角度转换为弧度 函数
     *
     * @param column 列
     * @return Radians
     */
    public static Radians radians(Cmd column) {
        Objects.requireNonNull(column);
        return new Radians(column);
    }

    /**
     * 求正弦值 函数
     *
     * @param column 列
     * @return Sin
     */
    public static Sin sin(Cmd column) {
        Objects.requireNonNull(column);
        return new Sin(column);
    }

    /**
     * 求反正弦值 函数
     *
     * @param column 列
     * @return Asin
     */
    public static Asin asin(Cmd column) {
        Objects.requireNonNull(column);
        return new Asin(column);
    }

    /**
     * 求余弦值 函数
     *
     * @param column 列
     * @return Cos
     */
    public static Cos cos(Cmd column) {
        Objects.requireNonNull(column);
        return new Cos(column);
    }

    /**
     * 求反余弦值 函数
     *
     * @param column 列
     * @return Acos
     */
    public static Acos acos(Cmd column) {
        Objects.requireNonNull(column);
        return new Acos(column);
    }

    /**
     * 求正切值 函数
     *
     * @param column 列
     * @return Tan
     */
    public static Tan tan(Cmd column) {
        Objects.requireNonNull(column);
        return new Tan(column);
    }

    /**
     * 求反正切值 函数
     *
     * @param column 列
     * @return Atan
     */
    public static Atan atan(Cmd column) {
        Objects.requireNonNull(column);
        return new Atan(column);
    }

    /**
     * 求余切值 函数
     *
     * @param column 列
     * @return Cot
     */
    public static Cot cot(Cmd column) {
        Objects.requireNonNull(column);
        return new Cot(column);
    }

    /**
     * 返回字符串的字符数
     *
     * @param column 列
     * @return CharLength
     */
    public static CharLength charLength(Cmd column) {
        Objects.requireNonNull(column);
        return new CharLength(column);
    }

    /**
     * 返回字符串的长度 函数
     *
     * @param column 列
     * @return Length
     */
    public static Length length(Cmd column) {
        Objects.requireNonNull(column);
        return new Length(column);
    }

    /**
     * 转换成大写 函数
     *
     * @param column 列
     * @return Upper
     */
    public static Upper upper(Cmd column) {
        Objects.requireNonNull(column);
        return new Upper(column);
    }

    /**
     * 转换成小写 函数
     *
     * @param column 列
     * @return Lower
     */
    public static Lower lower(Cmd column) {
        Objects.requireNonNull(column);
        return new Lower(column);
    }

    /**
     * 左边截取
     *
     * @param column 列
     * @return Left
     */
    public static Left left(Cmd column, int length) {
        Objects.requireNonNull(column);
        return new Left(column, length);
    }

    /**
     * 右边截取
     *
     * @param column 列
     * @return Right
     */
    public static Right right(Cmd column, int start) {
        Objects.requireNonNull(column);
        return new Right(column, start);
    }

    /**
     * 字符截取
     *
     * @param column 列
     * @param start 开始位置
     * @return SubStr
     */
    public static SubStr subStr(Cmd column, int start) {
        Objects.requireNonNull(column);
        return new SubStr(column, start);
    }

    /**
     * 字符截取
     *
     * @param column 列
     * @param start 开始位置
     * @param length 长度
     * @return SubStr
     */
    public static SubStr subStr(Cmd column, int start, int length) {
        Objects.requireNonNull(column);
        return new SubStr(column, start, length);
    }

    /**
     * 从左边开始填充
     *
     * @param column 列
     * @param length 填充后的长度
     * @param pad 填充内容
     * @return Lpad
     */
    public static Lpad lpad(Cmd column, int length, String pad) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(pad);
        return new Lpad(column, length, pad);
    }

    /**
     * 从左边开始填充
     *
     * @param column 列
     * @param length 填充后的长度
     * @param pad 填充内容
     * @return Rpad
     */
    public static Rpad rpad(Cmd column, int length, String pad) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(pad);
        return new Rpad(column, length, pad);
    }

    /**
     * 删除两边空格
     *
     * @param column 列
     * @return Trim
     */
    public static Trim trim(Cmd column) {
        Objects.requireNonNull(column);
        return new Trim(column);
    }

    /**
     * 删除左边空格
     *
     * @param column 列
     * @return Ltrim
     */
    public static Ltrim ltrim(Cmd column) {
        Objects.requireNonNull(column);
        return new Ltrim(column);
    }

    /**
     * 删除右边空格
     *
     * @param column 列
     * @return Rtrim
     */
    public static Rtrim rtrim(Cmd column) {
        Objects.requireNonNull(column);
        return new Rtrim(column);
    }

    /**
     * 字符串比较 函数
     * 返回 -1 0 1
     *
     * @param column 列
     * @param str 比较对象
     * @return Strcmp
     */
    public static Strcmp strcmp(Cmd column, String str) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(str);
        return new Strcmp(column, str);
    }

    /**
     * 将字符串  重复 n 次
     *
     * @param column 列
     * @param n 次数
     * @return Repeat
     */
    public static Repeat repeat(Cmd column, int n) {
        Objects.requireNonNull(column);
        return new Repeat(column, n);
    }

    /**
     * 替换 函数
     *
     * @param column 列
     * @param target      匹配目标
     * @param replacement 替换值
     * @return Replace
     */
    public static Replace replace(Cmd column, String target, String replacement) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(target);
        Objects.requireNonEmpty(replacement);
        return new Replace(column, target, replacement);
    }

    /**
     * 反转函数
     *
     * @param column 列
     * @return Reverse
     */
    public static Reverse reverse(Cmd column) {
        Objects.requireNonNull(column);
        return new Reverse(column);
    }

    /**
     * 匹配 match 在 key里边的位置
     * column 需要符合逗号分割规范
     *
     * @param column 列
     * @param match 匹配值
     * @return FindInSet
     */
    public static FindInSet mysqlFindInSet(Cmd column, String match) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(match);
        return new FindInSet(column, match);
    }

    /**
     * 匹配 match 在 key里边的位置
     * column 需要符合逗号分割规范
     *
     * @param column 列
     * @param match 匹配值
     * @return FindInSet
     */
    public static FindInSet mysqlFindInSet(Cmd column, Number match) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(match);
        return new FindInSet(column, match + "");
    }

    /**
     * 匹配key 在values里的位置 从1 开始
     *
     * @param column 列
     * @param values 指定顺利的数据（一组数据）
     * @return Field
     */
    @SafeVarargs
    public static Field mysqlFiled(Cmd column, Object... values) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(values);
        return new Field(column, values);
    }

    /**
     * 当前日期
     *
     * @return CurrentDate
     */
    public static CurrentDate currentDate() {
        return new CurrentDate();
    }

    /**
     * 当前时间（不包含日期）
     *
     * @return CurrentTime
     */
    public static CurrentTime currentTime() {
        return new CurrentTime();
    }

    /**
     * 当前时间（包含日期、时分秒）
     *
     * @return CurrentDateTime
     */
    public static CurrentDateTime currentDateTime() {
        return new CurrentDateTime();
    }

    /**
     * 获取年份
     *
     * @param column 列
     * @return Year
     */
    public static Year year(Cmd column) {
        Objects.requireNonNull(column);
        return new Year(column);
    }

    /**
     * 获取月份
     *
     * @param column 列
     * @return Month
     */
    public static Month month(Cmd column) {
        Objects.requireNonNull(column);
        return new Month(column);
    }

    /**
     * 获取日期部分，不包含时分秒
     *
     * @param column 列
     * @return DateFormat
     */
    public static DateFormat date(Cmd column) {
        Objects.requireNonNull(column);
        return new DateFormat(column, DatePattern.YYYY_MM_DD);
    }

    /**
     * 格式化时间
     *
     * @param column 列
     * @param pattern 格式
     * @return DateFormat
     */
    public static DateFormat dateFormat(Cmd column, String pattern) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(pattern);
        return new DateFormat(column, pattern);
    }

    /**
     * 格式化时间
     *
     * @param column 列
     * @param pattern 格式
     * @return DateFormat
     */
    public static DateFormat dateFormat(Cmd column, DatePattern pattern) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(pattern);
        return new DateFormat(column, pattern);
    }

    /**
     * 获取column的第几天
     *
     * @param column 列
     * @return Day
     */
    public static Day day(Cmd column) {
        Objects.requireNonNull(column);
        return new Day(column);
    }

    /**
     * 获取column的星期几
     *
     * @param column 列
     * @return Weekday
     */
    public static Weekday weekday(Cmd column) {
        Objects.requireNonNull(column);
        return new Weekday(column);
    }

    /**
     * 获取column的小时部分
     *
     * @param column 列
     * @return Hour
     */
    public static Hour hour(Cmd column) {
        Objects.requireNonNull(column);
        return new Hour(column);
    }

    /**
     * 日期比较
     *
     * @param column 列
     * @return DateDiff
     */
    public static DateDiff dateDiff(Cmd column, Cmd another) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(another);
        return new DateDiff(column, another);
    }

    /**
     * 日期增加
     *
     * @param column 列
     * @return DateAdd
     */
    public static DateAdd dateAdd(Cmd column, int n, TimeUnit timeUnit) {
        Objects.requireNonNull(column);
        return new DateAdd(column, n, timeUnit);
    }

    /**
     * md5
     *
     * @param str
     * @return Md5
     */
    public static Md5 mysqlMd5(String str) {
        Objects.requireNonEmpty(str);
        return new Md5(str);
    }

    /**
     * md5
     *
     * @param column 列
     * @return Md5
     */
    public static Md5 mysqlMd5(Cmd column) {
        Objects.requireNonNull(column);
        return new Md5(column);
    }

    /**
     * 将ip转成数字
     *
     * @param ip
     * @return InetAton
     */
    public static InetAton inetAton(String ip) {
        Objects.requireNonEmpty(ip);
        return new InetAton(ip);
    }

    /**
     * 将ip转成数字
     *
     * @param column 列
     * @return InetAton
     */
    public static InetAton inetAton(Cmd column) {
        Objects.requireNonNull(column);
        return new InetAton(column);
    }

    /**
     * 将ip数字转成ip
     *
     * @param ipNumber
     * @return InetNtoa
     */
    public static InetNtoa inetNtoa(Number ipNumber) {
        Objects.requireNonNull(ipNumber);
        return new InetNtoa(ipNumber);
    }

    /**
     * 将ip数字转成ip
     *
     * @param column 列
     * @return InetNtoa
     */
    public static InetNtoa inetNtoa(Cmd column) {
        Objects.requireNonNull(column);
        return new InetNtoa(column);
    }

    /**
     * concat拼接 函数
     *
     * @param column 列
     * @param values 需要拼接的值
     * @return Concat
     */
    @SafeVarargs
    public static Concat concat(Cmd column, Object... values) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(values);
        return new Concat(column, values);
    }

    /**
     * concatWs拼接 函数
     *
     * @param column 列
     * @param split 分隔符
     * @param values 需要拼接的值
     * @return ConcatWs
     */
    @SafeVarargs
    public static ConcatWs concatWs(Cmd column, String split, Object... values) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(split);
        Objects.requireNonNull(values);
        return new ConcatWs(column, split, values);
    }

    /* --------------------------------------以下为判断条件----------------------------------------------*/

    /**
     * if(条件,值1,值2) 函数
     *
     * @param condition 条件
     * @param value condition符合时的值
     * @param thenValue condition不符合时的值
     * @return If
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
     * @param condition 条件
     * @param value condition符合时的值
     * @param thenValue condition不符合时的值
     * @return If
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
     * @param condition 条件
     * @param value condition符合时的值
     * @param thenValue condition不符合时的值
     * @return If
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
     * @param condition 条件
     * @param value condition符合时的值
     * @param thenValue condition不符合时的值
     * @return If
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
     * @param column 列
     * @param value 值
     * @return IfNull
     */
    public static IfNull ifNull(Cmd column, Cmd value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new IfNull(column, value);
    }

    /**
     * IFNULL(条件,值1,值2) 函数
     *
     * @param column 列
     * @param value 值
     * @return IfNull
     */
    public static IfNull ifNull(Cmd column, Serializable value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new IfNull(column, value);
    }

    /**
     * column 列 is NOT NULL
     *
     * @param column 列
     * @return IsNull
     */
    public static IsNull isNull(Cmd column) {
        Objects.requireNonNull(column);
        return new IsNull(column);
    }

    /**
     * column 列 is NOT NULL
     *
     * @param column 列
     * @return IsNotNull
     */
    public static IsNotNull isNotNull(Cmd column) {
        Objects.requireNonNull(column);
        return new IsNotNull(column);
    }

    /**
     * column列 为空
     *
     * @param column 列
     * @return Eq
     */
    public static Eq isEmpty(Cmd column) {
        Objects.requireNonNull(column);
        return new Eq(column, SqlConst.EMPTY);
    }

    /**
     * column 列 不为空
     *
     * @param column 列
     * @return Ne
     */
    public static Ne isNotEmpty(Cmd column) {
        Objects.requireNonNull(column);
        return new Ne(column, SqlConst.EMPTY);
    }

    /**
     * case 语句块
     *
     * @return Case
     */
    public static Case case_() {
        return new Case();
    }

    /**
     * eq等于 判断
     *@param column 列
     * @return Eq
     */
    public static Eq eq(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Eq(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * ne不等于 判断
     *@param column 列
     * @return Ne
     */
    public static Ne ne(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Ne(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * 不为空 判断
     *@param column 列
     * @return NotEmpty
     */
    public static NotEmpty notEmpty(Cmd column) {
        Objects.requireNonNull(column);
        return new NotEmpty(column);
    }

    /**
     * 为空 判断
     *@param column 列
     * @return Empty
     */
    public static Empty empty(Cmd column) {
        Objects.requireNonNull(column);
        return new Empty(column);
    }

    /**
     * gt大于 判断
     * @param column 列
     * @param value 比较对象
     * @return Gt
     */
    public static Gt gt(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Gt(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * gte大于等于 判断
     * @param column 列
     * @param value 比较对象
     * @return Gte
     */
    public static Gte gte(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Gte(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * gt小于 判断
     * @param column 列
     * @param value 比较对象
     * @return Lt
     */
    public static Lt lt(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Lt(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * gt小于等于 判断
     * @param column 列
     *@param value 比较对象
     * @return Lte
     */
    public static Lte lte(Cmd column, Object value) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        return new Lte(column, paramWrapAndConvertToCmd(column, value));
    }

    /**
     * in 多个值
     * @param column 列
     * @param values 包含的指定值
     * @return In
     */
    @SafeVarargs
    public static In in(Cmd column, Object... values) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(values);
        Cmd[] cmds = new Cmd[values.length];
        for (int i = 0; i < values.length; i++) {
            cmds[i] = paramWrapAndConvertToCmd(column, values[i]);
        }
        return new In(column).add(cmds);
    }

    /**
     * in 多个值
     * @param column 列
     * @param values 包含的指定值
     * @return In
     */
    public static In in(Cmd column, Collection<?> values) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(values);
        return new In(column).add(values.stream().map(item -> paramWrapAndConvertToCmd(column, item)).collect(Collectors.toList()));
    }

    /**
     * in 一个查询
     *  @param column 列
     * @param query 子查询
     * @return In
     */
    public static In in(Cmd column, IQuery query) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(query);
        return new In(column).add(query);
    }

    /**
     * not in 多个值
     * @param column 列
     * @return NotIn
     */
    @SafeVarargs
    public static NotIn notIn(Cmd column, Object... values) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(values);
        Cmd[] cmds = new Cmd[values.length];
        for (int i = 0; i < values.length; i++) {
            cmds[i] = paramWrapAndConvertToCmd(column, values[i]);
        }
        return new NotIn(column).add(cmds);
    }

    /**
     * not in 多个值
     * @param column 列
     * @return NotIn
     */
    public static NotIn notIn(Cmd column, Collection<?> values) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(values);
        return new NotIn(column).add(values.stream().map(item -> paramWrapAndConvertToCmd(column, item)).collect(Collectors.toList()));
    }

    /**
     * not in 一个查询
     * @param column 列
     * @return NotIn
     */
    public static NotIn notIn(Cmd column, IQuery query) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(query);
        return new NotIn(column).add(query);
    }

    /**
     * exists 一个查询
     *
     * @param query 查询
     * @return Exists
     */
    public static Exists exists(IQuery query) {
        Objects.requireNonNull(query);
        return new Exists(query);
    }

    /**
     * not exists 一个查询
     *
     * @param query 查询
     * @return NotExists
     */
    public static NotExists notExists(IQuery query) {
        Objects.requireNonNull(query);
        return new NotExists(query);
    }

    /**
     * between 区间判断
     *
     * @param column 列
     * @param value 开始值
     * @param value2 结束值
     * @return Between
     */
    public static Between between(Cmd column, Serializable value, Serializable value2) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        Objects.requireNonNull(value2);
        return new Between(column, paramWrapAndConvertToCmd(column, value), paramWrapAndConvertToCmd(column, value2));
    }

    /**
     * not between 区间判断
     *
     * @param column 列
     * @param value 值
     * @param value2
     * @return NotBetween
     */
    public static NotBetween notBetween(Cmd column, Serializable value, Serializable value2) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(value);
        Objects.requireNonNull(value2);
        return new NotBetween(column, paramWrapAndConvertToCmd(column, value), paramWrapAndConvertToCmd(column, value2));
    }

    /**
     * like 判断
     *
     * @param column 列
     * @param value 值
     * @return Like
     */
    public static Like like(Cmd column, Object value) {
        return like(LikeMode.DEFAULT, column, value);
    }

    /**
     * like 判断
     *
     * @param column 列
     * @param value 值
     * @return Like
     */
    public static Like like(LikeMode mode, Cmd column, Object value) {
        Objects.requireNonNull(column);
        Object wrapValue = likeParamWrap(column, value, mode, false);
        if (wrapValue instanceof Object[]) {
            Object[] values = (Object[]) wrapValue;
            mode = (LikeMode) values[0];
            value = values[1];
        }
        return new Like(mode, column, value);
    }

    /**
     * notLike 判断
     *
     * @param column 列
     * @param value 值
     * @return NotLike
     */
    public static NotLike notLike(Cmd column, Object value) {
        return notLike(LikeMode.DEFAULT, column, value);
    }

    /**
     * not like 判断
     *
     * @param column 列
     * @param value 值
     * @return NotLike
     */
    public static NotLike notLike(LikeMode mode, Cmd column, Object value) {
        Objects.requireNonNull(column);
        Object wrapValue = likeParamWrap(column, value, mode, true);
        if (wrapValue instanceof Object[]) {
            Object[] values = (Object[]) wrapValue;
            mode = (LikeMode) values[0];
            value = values[1];
        }
        return new NotLike(mode, column, value);
    }

    /**
     * mysql fromUnixTime 函数
     *
     * @param column 列
     * @return FromUnixTime
     */
    public static FromUnixTime mysqlFromUnixTime(Cmd column) {
        Objects.requireNonNull(column);
        return new FromUnixTime(column);
    }

    /**
     * mysql fromUnixTime 函数
     *
     * @param column          列
     * @param containValue 包含值
     * @return JsonContains
     */
    public static JsonContains mysqlJsonContains(Cmd column, Serializable containValue) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(containValue);
        return new JsonContains(column, containValue);
    }

    /**
     * mysql json 是否包含值
     *
     * @param column          列
     * @param containValue 包含值
     * @param path         指定路径
     * @return JsonContains
     */
    public static JsonContains mysqlJsonContains(Cmd column, Serializable containValue, String path) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(containValue);
        Objects.requireNonEmpty(path);
        return new JsonContains(column, containValue, path);
    }

    /**
     * mysql json 是否包含路径
     *
     * @param column   列
     * @param paths 指定路径
     * @return JsonContainsPath
     */
    @SafeVarargs
    public static JsonContainsPath mysqlJsonContainsPath(Cmd column, String... paths) {
        return mysqlJsonContainsPath(column, true, paths);
    }

    /**
     * mysql json 是否包含路径
     *
     * @param column      列
     * @param allMatch 是否全匹配上
     * @param paths    指定路径
     * @return JsonContainsPath
     */
    @SafeVarargs
    public static JsonContainsPath mysqlJsonContainsPath(Cmd column, boolean allMatch, String... paths) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(paths);
        return new JsonContainsPath(column, allMatch, paths);
    }

    /**
     * mysql json 值提取操作
     * @param column 列
     * @param paths 指定路径
     * @return JsonExtract
     */
    @SafeVarargs
    public static JsonExtract mysqlJsonExtract(Cmd column, String... paths) {
        Objects.requireNonNull(column);
        Objects.requireNonNull(paths);
        return new JsonExtract(column, paths);
    }

    /**
     * 返回目标字符(串)在源字符串字符串中出现的起始位置
     *
     * @param column 列
     * @param str 匹配的字符
     * @return Instr
     */
    public static Instr instr(Cmd column, String str) {
        Objects.requireNonNull(column);
        Objects.requireNonEmpty(str);
        return new Instr(column, str);
    }

    /**
     * 获得 mysql的函数聚合类
     *
     * @param column 列，后续可以以此列操作
     * @return MysqlFunctions
     */
    public MysqlFunctions mysql(Cmd column) {
        return new MysqlFunctions(column);
    }
}
