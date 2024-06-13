package db.sql.api.tookit;


import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CmdUtils {

    public static StringBuilder join(SqlBuilderContext context, StringBuilder builder, List<? extends Cmd> cmdList) {
        return join(null, null, context, builder, cmdList);
    }

    public static StringBuilder join(Cmd module, Cmd user, SqlBuilderContext context, StringBuilder builder, List<? extends Cmd> cmdList) {
        return join(module, user, context, builder, cmdList, null);
    }

    public static StringBuilder join(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder builder, List<? extends Cmd> cmdList, char[] delimiter) {
        if (cmdList == null) {
            return builder;
        }
        Iterator<? extends Cmd> iterator = cmdList.iterator();
        if (!iterator.hasNext()) {
            return builder;
        }
        while (true) {
            Cmd cmd = iterator.next();
            builder = cmd.sql(module, parent, context, builder);
            if (!iterator.hasNext()) {
                break;
            }
            if (delimiter != null) {
                builder.append(delimiter);
            }
        }
        return builder;
    }

    public static StringBuilder join(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder builder, Cmd[] cmds, char[] delimiter) {
        if (cmds == null || cmds.length < 1) {
            return builder;
        }
        int length = cmds.length;
        for (int i = 0; i < length; i++) {
            if (i != 0 && delimiter != null) {
                builder.append(delimiter);
            }
            builder = cmds[i].sql(module, parent, context, builder);
        }
        return builder;
    }

    public static StringBuilder join(StringBuilder builder, String[] strs, char[] delimiter) {
        if (strs == null || strs.length < 1) {
            return builder;
        }
        int length = strs.length;
        for (int i = 0; i < length; i++) {
            if (i != 0 && delimiter != null) {
                builder.append(delimiter);
            }
            builder.append(strs[i]);
        }
        return builder;
    }

    public static boolean contain(Cmd cmd, Object... params) {
        if (Objects.isNull(params)) {
            return false;
        }
        for (Object param : params) {
            if (contain(cmd, param)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contain(Cmd cmd, Object another) {
        if (Objects.isNull(another)) {
            return false;
        }
        if (cmd == another) {
            return true;
        }
        if (another instanceof Cmd) {
            return contain(cmd, (Cmd) another);
        } else if (another instanceof List) {
            return contain(cmd, (List) another);
        } else if (another instanceof Object[]) {
            return contain(cmd, (Object[]) another);
        }
        return false;
    }

    public static boolean contain(Cmd cmd, Cmd another) {
        if (Objects.isNull(another)) {
            return false;
        }
        if (cmd == another) {
            return true;
        }
        return another.contain(cmd);
    }

    public static boolean contain(Cmd cmd, List<?> another) {
        if (Objects.isNull(another)) {
            return false;
        }
        for (Object param : another) {
            if (contain(cmd, param)) {
                return true;
            }
        }

        return false;
    }
}
