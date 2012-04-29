package jp.tacores.mankitu;

public interface IBackupAccess {
	void importBackup(IContextContainer context);
	void exportBackup(IContextContainer context);
}
