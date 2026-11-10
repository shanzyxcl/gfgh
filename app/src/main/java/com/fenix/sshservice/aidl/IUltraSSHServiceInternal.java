package com.fenix.sshservice.aidl;

public interface IUltraSSHServiceInternal extends android.os.IInterface {

  public abstract static class Stub extends android.os.Binder
      implements com.fenix.sshservice.aidl.IUltraSSHServiceInternal {
    private static final java.lang.String DESCRIPTOR = "com.fenix.sshservice.aidl.IUltraSSHServiceInternal";

    public Stub() {
      this.attachInterface(this, DESCRIPTOR);
    }

    public static com.fenix.sshservice.aidl.IUltraSSHServiceInternal asInterface(
        android.os.IBinder obj) {
      if ((obj == null)) {
        return null;
      }

      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin instanceof IUltraSSHServiceInternal))) {
        return ((com.fenix.sshservice.aidl.IUltraSSHServiceInternal) iin);
      }
      return new com.fenix.sshservice.aidl.IUltraSSHServiceInternal.Stub.Proxy(obj);
    }

    @Override
    public android.os.IBinder asBinder() {
      return this;
    }

    @Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
        throws android.os.RemoteException {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code) {
        case INTERFACE_TRANSACTION:
          {
            reply.writeString(descriptor);
            return true;
          }
        case TRANSACTION_stopVPN:
          {
            data.enforceInterface(descriptor);
            this.stopVPN();
            reply.writeNoException();
            return true;
          }
        default:
          {
            return super.onTransact(code, data, reply, flags);
          }
      }
    }

    private static class Proxy implements com.fenix.sshservice.aidl.IUltraSSHServiceInternal {
      private final android.os.IBinder mRemote;

      Proxy(android.os.IBinder remote) {
        mRemote = remote;
      }

      @Override
      public android.os.IBinder asBinder() {
        return mRemote;
      }

      public java.lang.String getInterfaceDescriptor() {
        return DESCRIPTOR;
      }

      @Override
      public void stopVPN() throws android.os.RemoteException {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          mRemote.transact(Stub.TRANSACTION_stopVPN, _data, _reply, 0);
          _reply.readException();
        } finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }

    static final int TRANSACTION_stopVPN = (android.os.IBinder.FIRST_CALL_TRANSACTION);
  }
  void stopVPN() throws android.os.RemoteException;
}
