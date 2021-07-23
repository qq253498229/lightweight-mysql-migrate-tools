select a.TABLE_SCHEMA as CONSTRAINT_SCHEMA,
       a.INDEX_NAME   as CONSTRAINT_NAME,
       a.TABLE_NAME,
       a.COLUMN_NAME,
       a.SEQ_IN_INDEX,
       b.POSITION_IN_UNIQUE_CONSTRAINT,
       b.REFERENCED_TABLE_SCHEMA,
       b.REFERENCED_TABLE_NAME,
       b.REFERENCED_COLUMN_NAME,

       a.NON_UNIQUE,
       a.INDEX_SCHEMA,
       a.COLLATION,
       a.CARDINALITY,
       a.SUB_PART,
       a.PACKED,
       a.NULLABLE,
       a.INDEX_TYPE,
       a.COMMENT,
       a.INDEX_COMMENT
from information_schema.STATISTICS a
         left join information_schema.KEY_COLUMN_USAGE b
                   on a.TABLE_SCHEMA = b.TABLE_SCHEMA and a.TABLE_NAME = b.TABLE_NAME and
                      a.INDEX_NAME = b.CONSTRAINT_NAME and a.COLUMN_NAME = b.COLUMN_NAME
where a.TABLE_SCHEMA = ?