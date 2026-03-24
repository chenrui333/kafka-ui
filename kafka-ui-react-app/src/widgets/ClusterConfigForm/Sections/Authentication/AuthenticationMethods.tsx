import React from 'react';
import Input from 'components/common/Input/Input';
import Checkbox from 'components/common/Checkbox/Checkbox';
import Fileupload from 'widgets/ClusterConfigForm/common/Fileupload';
import SSLForm from 'widgets/ClusterConfigForm/common/SSLForm';
import Credentials from 'widgets/ClusterConfigForm/common/Credentials';

const AuthenticationMethods: React.FC<{ method: string }> = ({ method }) => {
  switch (method) {
    case 'SASL/JAAS':
      return (
        <>
          <Input
            type="text"
            name="auth.props.saslJaasConfig"
            label="sasl.jaas.config"
            withError
          />
          <Input
            type="text"
            name="auth.props.saslMechanism"
            label="sasl.mechanism"
            withError
          />
        </>
      );
    case 'SASL/GSSAPI':
      return (
        <>
          <Input
            label="Kerberos service name"
            type="text"
            name="auth.props.saslKerberosServiceName"
            withError
          />
          <Checkbox name="auth.props.storeKey" label="Store Key" />
          <Fileupload name="auth.props.keyTabFile" label="Key Tab (optional)" />
          <Input
            type="text"
            name="auth.props.principal"
            label="Principal *"
            withError
          />
        </>
      );
    case 'SASL/OAUTHBEARER':
      return (
        <Input
          label="Unsecured Login String Claim_sub *"
          type="text"
          name="auth.props.unsecuredLoginStringClaim_sub"
          withError
        />
      );
    case 'SASL/PLAIN':
    case 'SASL/SCRAM-256':
    case 'SASL/SCRAM-512':
    case 'SASL/LDAP':
      return <Credentials prefix="auth.props" />;
    case 'Delegation tokens':
      return (
        <>
          <Input
            label="Token Id"
            type="text"
            name="auth.props.tokenId"
            withError
          />
          <Input
            label="Token Value *"
            type="text"
            name="auth.props.tokenValue"
            withError
          />
        </>
      );
    case 'SASL/AWS IAM':
      return (
        <>
          <Input
            label="AWS Profile Name"
            type="text"
            name="auth.props.awsProfileName"
            withError
            hint="Optional. Leave all AWS IAM fields blank to use the pod or service-account role from the default AWS credential chain, including EKS IRSA."
          />
          <Input
            label="AWS Role ARN"
            type="text"
            name="auth.props.awsRoleArn"
            withError
            hint="Optional. Use this when kafka-ui should assume a dedicated role before connecting to MSK."
          />
          <Input
            label="AWS Role Session Name"
            type="text"
            name="auth.props.awsRoleSessionName"
            withError
            hint="Optional. Useful for a stable session identity when assuming a role for MSK IAM auth."
          />
          <Input
            label="AWS STS Region"
            type="text"
            name="auth.props.awsStsRegion"
            withError
            hint="Optional. Set this when STS must use a regional endpoint, for example through a VPC endpoint."
          />
        </>
      );
    case 'mTLS':
      return <SSLForm prefix="auth.keystore" title="Keystore" />;
    default:
      return null;
  }
};

export default AuthenticationMethods;
