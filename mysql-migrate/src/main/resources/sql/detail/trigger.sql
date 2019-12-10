SELECT TRIGGER_CATALOG,
       TRIGGER_SCHEMA,
       TRIGGER_NAME,
       EVENT_MANIPULATION,
       EVENT_OBJECT_CATALOG,
       EVENT_OBJECT_SCHEMA,
       EVENT_OBJECT_TABLE,
       ACTION_ORDER,
       ACTION_CONDITION,
       ACTION_STATEMENT,
       ACTION_ORIENTATION,
       ACTION_TIMING,
       ACTION_REFERENCE_OLD_TABLE,
       ACTION_REFERENCE_NEW_TABLE,
       ACTION_REFERENCE_OLD_ROW,
       ACTION_REFERENCE_NEW_ROW,
       CREATED,
       SQL_MODE,
       DEFINER,
       CHARACTER_SET_CLIENT,
       COLLATION_CONNECTION,
       DATABASE_COLLATION
FROM information_schema.TRIGGERS
WHERE TRIGGER_SCHEMA = ?