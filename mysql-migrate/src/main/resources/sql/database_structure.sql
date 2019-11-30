select distinct s.SCHEMA_NAME,
                s.DEFAULT_CHARACTER_SET_NAME,
                s.DEFAULT_COLLATION_NAME,
                t.TABLE_NAME,
                t.ENGINE,
                ch.CHARACTER_SET_NAME,
                t.TABLE_COLLATION,
                t.TABLE_COMMENT,
                c.COLUMN_NAME,
                c.ORDINAL_POSITION,
                c.COLUMN_DEFAULT,
                c.IS_NULLABLE,
                c.DATA_TYPE,
                c.CHARACTER_MAXIMUM_LENGTH,
                c.CHARACTER_OCTET_LENGTH,
                c.NUMERIC_PRECISION,
                c.NUMERIC_SCALE,
                c.DATETIME_PRECISION,
                c.CHARACTER_SET_NAME,
                c.COLLATION_NAME,
                c.COLUMN_TYPE,
                c.COLUMN_KEY,
                c.EXTRA,
                c.COLUMN_COMMENT,
                k.CONSTRAINT_SCHEMA,
                k.CONSTRAINT_NAME,
                k.ORDINAL_POSITION,
                k.POSITION_IN_UNIQUE_CONSTRAINT,
                k.REFERENCED_TABLE_SCHEMA,
                k.REFERENCED_TABLE_NAME,
                k.REFERENCED_COLUMN_NAME
from information_schema.SCHEMATA s
         left join information_schema.TABLES t on t.TABLE_SCHEMA = s.SCHEMA_NAME
         left join information_schema.COLLATION_CHARACTER_SET_APPLICABILITY ch on t.TABLE_COLLATION = ch.COLLATION_NAME
         LEFT JOIN information_schema.COLUMNS c ON c.TABLE_NAME = t.TABLE_NAME
         left join information_schema.KEY_COLUMN_USAGE k on k.TABLE_NAME = t.TABLE_NAME
where s.SCHEMA_NAME = #{database_name};
