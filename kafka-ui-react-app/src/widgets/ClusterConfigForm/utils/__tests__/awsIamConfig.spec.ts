import formSchema from 'widgets/ClusterConfigForm/schema';
import { ClusterConfigFormValues } from 'widgets/ClusterConfigForm/types';
import { getInitialFormData } from 'widgets/ClusterConfigForm/utils/getInitialFormData';
import { transformFormDataToPayload } from 'widgets/ClusterConfigForm/utils/transformFormDataToPayload';

const getBaseFormValues = (): ClusterConfigFormValues => ({
  name: 'msk-eks',
  readOnly: false,
  bootstrapServers: [
    { host: 'b-1.example.kafka.us-east-1.amazonaws.com', port: '9098' },
  ],
  customAuth: {},
});

describe('AWS IAM cluster config helpers', () => {
  it('builds bare AWS IAM JAAS config for ambient credentials', () => {
    const payload = transformFormDataToPayload({
      ...getBaseFormValues(),
      auth: {
        method: 'SASL/AWS IAM',
        securityProtocol: 'SASL_SSL',
        props: {},
      },
    });

    expect(payload.properties).toEqual({
      'security.protocol': 'SASL_SSL',
      'sasl.mechanism': 'AWS_MSK_IAM',
      'sasl.client.callback.handler.class':
        'software.amazon.msk.auth.iam.IAMClientCallbackHandler',
      'sasl.jaas.config':
        'software.amazon.msk.auth.iam.IAMLoginModule required;',
    });
  });

  it('adds optional assume-role fields to AWS IAM JAAS config', () => {
    const payload = transformFormDataToPayload({
      ...getBaseFormValues(),
      auth: {
        method: 'SASL/AWS IAM',
        securityProtocol: 'SASL_SSL',
        props: {
          awsRoleArn: 'arn:aws:iam::123456789012:role/msk-ui',
          awsRoleSessionName: 'kafka-ui',
          awsStsRegion: 'us-east-1',
        },
      },
    });

    expect(payload.properties?.['sasl.jaas.config']).toBe(
      'software.amazon.msk.auth.iam.IAMLoginModule required' +
        ' awsRoleArn="arn:aws:iam::123456789012:role/msk-ui"' +
        ' awsRoleSessionName="kafka-ui"' +
        ' awsStsRegion="us-east-1";'
    );
  });

  it('parses AWS IAM properties back into the structured auth form', () => {
    const initialValues = getInitialFormData({
      name: 'msk-eks',
      readOnly: false,
      bootstrapServers: 'b-1.example.kafka.us-east-1.amazonaws.com:9098',
      properties: {
        'security.protocol': 'SASL_SSL',
        'sasl.mechanism': 'AWS_MSK_IAM',
        'sasl.client.callback.handler.class':
          'software.amazon.msk.auth.iam.IAMClientCallbackHandler',
        'sasl.jaas.config':
          'software.amazon.msk.auth.iam.IAMLoginModule required' +
          ' awsRoleArn="arn:aws:iam::123456789012:role/msk-ui"' +
          ' awsRoleSessionName="kafka-ui"' +
          ' awsStsRegion="us-east-1";',
      },
    } as never);

    expect(initialValues.auth).toEqual({
      method: 'SASL/AWS IAM',
      securityProtocol: 'SASL_SSL',
      props: {
        awsProfileName: undefined,
        awsRoleArn: 'arn:aws:iam::123456789012:role/msk-ui',
        awsRoleSessionName: 'kafka-ui',
        awsStsRegion: 'us-east-1',
      },
    });
    expect(initialValues.customAuth).toEqual({});
  });

  it('requires a role arn when advanced assume-role fields are set', async () => {
    await expect(
      formSchema.validate({
        ...getBaseFormValues(),
        auth: {
          method: 'SASL/AWS IAM',
          securityProtocol: 'SASL_SSL',
          props: {
            awsRoleSessionName: 'kafka-ui',
          },
        },
      })
    ).rejects.toThrow(
      'AWS Role ARN is required when AWS Role Session Name is set'
    );
  });
});
