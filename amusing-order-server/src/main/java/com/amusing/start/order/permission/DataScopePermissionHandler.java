package com.amusing.start.order.permission;

import cn.hutool.core.collection.CollectionUtil;
import com.amusing.start.code.CommunalCode;
import com.amusing.start.exception.CustomException;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @author Lv.QingYu
 * @since 2025/3/3
 */
@Slf4j
public class DataScopePermissionHandler implements MultiDataPermissionHandler {

    private static final String DELIMITER = ",";

    @Override
    public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
        List<String> userIdList = DataScopeHolder.get();
        if (CollectionUtil.isEmpty(userIdList)) {
            return null;
        }
        String tableName = table.getAlias() == null ? table.getName() : table.getAlias().getName();
        String idStr = userIdList.stream().filter(Objects::nonNull).collect(Collectors.joining(DELIMITER));
        try {
            return CCJSqlParserUtil.parseCondExpression(tableName + ".create_by in (" + idStr + ")");
        } catch (JSQLParserException e) {
            log.error("[DataPermissionInterceptor]-err! msg:{}", Throwables.getStackTraceAsString(e));
            throw new CustomException(CommunalCode.SERVICE_ERR);
        }
    }

}
