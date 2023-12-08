export API_URL_SSM="`aws ssm get-parameter --name /<?app-name>/${{ github.ref_name }}/api/url`"
sudo apt-get install jq
export API_URL=`echo $API_URL_SSM | jq -r '.Parameter.Value'`
curl --fail "$API_URL"$1