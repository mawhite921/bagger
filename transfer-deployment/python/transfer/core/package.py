import os
import re
from transfer import utils

class PackageModeler():

    def __init__(self, config):
        self.project_name = "packagemodeler-core"
        self.db_name = "package_modeler"
        self.roles = {
            'reader': 'package_modeler_reader_role',
            'writer': 'package_modeler_data_writer_role',
            'transfer_reader': 'transfer_reader_user',
            'transfer_writer': 'transfer_data_writer_user',
        }
        self.install_dir = config['TRANSFER_INSTALL_DIR'] if config['TRANSFER_INSTALL_DIR'] else "."
        self.driver_package = "files/%s-%s-bin.zip" % (self.project_name, config['VERSION'])
        self.driver = "%s/%s-%s/bin/fixturedriver" % (self.install_dir, self.project_name, config['VERSION'])
        self.debug = config['DEBUG'] if config['DEBUG'] else False
        self.psql = config['PSQL'] if config['PSQL'] else "/usr/bin/psql"
        self.db_prefix = config['DB_PREFIX'] + "_" if config['DB_PREFIX'] else ""
        self.role_prefix = config['ROLE_PREFIX'] + "_" if config['ROLE_PREFIX'] else ""
        self.passwds = {
            'transfer_reader': config['TRANSFER_READER_PASSWD'] if config['TRANSFER_READER_PASSWD'] else "transfer_reader_user",
            'transfer_writer': config['TRANSFER_WRITER_PASSWD'] if config['TRANSFER_WRITER_PASSWD'] else "transfer_data_writer_user",
        }
        self.create_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['create']
        self.roles_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['roles']
        self.tables_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['tables']        
        self.perms_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['perms']
        self.fixtures_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['fixtures']
        self.drop_sql_file = config['SQL_FILES_LOCATION'] + "/" + config['PM_CORE_SQL_FILES']['drop']
        self.hibernate_writer_props = """hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
                                         hibernate.connection.driver_class=org.postgresql.Driver
                                         hibernate.connection.url=jdbc:postgresql://%s:%s/%s
                                         hibernate.connection.username=%s
                                         hibernate.connection.password=%s
                                      """ % (config['PGHOST'], config['PGPORT'], self.db_prefix + self.db_name, 
                                             self.roles['transfer_writer'], self.passwds['transfer_writer'])
        self.hibernate_conf = "%s/%s-%s/conf/data_writer.packagemodeler.hibernate.properties" % (
            self.install_dir, self.project_name, config['VERSION']
        )
        os.environ['PGUSER'] = config['PGUSER'] if config['PGUSER'] else 'postgres'
        os.environ['PGHOST'] = config['PGHOST'] if config['PGHOST'] else 'localhost'
        os.environ['PGPORT'] = config['PGPORT'] if config['PGPORT'] else '5432'
        os.environ['PGPASSWORD'] = config['PGPASSWORD'] if config['PGPASSWORD'] else ""

    def create_database(self):
        """ creates database """
        os.environ['PGDATABASE'] = "postgres"
        if utils.list_databases(self.psql, self.debug).find(self.db_prefix + self.db_name) != -1:
            return "ERROR:  *** The %s database exists!" % (self.db_prefix + self.db_name)
        sql = self.__prefix_database(file(self.create_sql_file).read())
        result = utils.load_sqlstr(self.psql, sql, self.debug)
        return "Creating %s database\n=====================\n%s" % (self.project_name, result)

    def create_roles(self):
        """ populates database roles """
        os.environ['PGDATABASE'] = "postgres"
        sql = self.__prefix_roles(file(self.roles_sql_file).read(), self.roles)
        sql = self.__replace_passwds(sql)
        result = utils.load_sqlstr(self.psql, sql, self.debug)
        return "Creating %s roles\n===================\n%s" % (self.project_name, result)

    def create_tables(self):
        """ creates database tables """
        os.environ['PGDATABASE'] = self.db_prefix + self.db_name
        result = utils.load_sqlfile(self.psql, self.tables_sql_file, self.debug)
        return "Creating %s tables\n======================\n%s" % (self.project_name, result)

    def grant_permissions(self):
        """ grants database permissions """
        os.environ['PGDATABASE'] = self.db_prefix + self.db_name
        sql = self.__prefix_database(file(self.perms_sql_file).read())
        sql = self.__prefix_roles(sql, self.roles)
        result = utils.load_sqlstr(self.psql, sql, self.debug)
        return "Granting %s privileges\n====================\n%s" % (self.project_name, result)

    def drop(self):
        """ drops database and roles """
        os.environ['PGDATABASE'] = "postgres"
        sql = self.__prefix_database(file(self.drop_sql_file).read())
        sql = self.__prefix_roles(sql, self.roles)
        result = utils.load_sqlstr(self.psql, sql, self.debug)
        return "Dropping %s\n====================\n%s" % (self.project_name, result)

    def create_fixtures(self, project, env):
        """ creates database fixtures """
        os.environ['PGDATABASE'] = self.db_prefix + self.db_name
        fixtures_file = self.fixtures_sql_file.replace("-fixtures", "-%s-%s-fixtures" % (project, env))
        sql = file(fixtures_file).read()
        result = utils.load_sqlstr(self.psql, sql, self.debug)
        return "Installing %s database fixtures\n====================\n%s" % (self.project_name, result)

    def deploy_drivers(self):
        """ deploys command-line drivers """
        utils.unzip(self.driver_package, self.install_dir)
        utils.chmod("+x", self.driver)
        utils.strtofile(self.hibernate_writer_props, self.hibernate_conf)
        #utils.strtofile(self.hibernate_fixture_props, self.hibernate_fixture_conf)
        return "Deploying %s drivers\n====================\n%s" % (self.project_name, result)

    def __prefix_database(self, file):
        """ prepends db_prefix to database names """
        pattern = r'DATABASE (%s)' % self.db_name
        replacement = r'DATABASE %s\1' % self.db_prefix
        return re.sub(pattern, replacement, file)

    def __prefix_roles(self, file, roles):
        """ prepends role_prefix to role names """
        pattern = r'(%s)' % "|".join(roles.values())
        replacement = r'%s\1' % self.role_prefix
        return re.sub(pattern, replacement, file)

    def __replace_passwds(self, file):
        """ replaces passwords in sql dumps with values from config """
        def getrepl(match):
            return self.passwds.get(match.group(1))
        pattern = r'(\w+)_passwd'
        return re.sub(pattern, getrepl, file)